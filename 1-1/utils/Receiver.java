package com.dsu.noncloud.utils;

import org.springframework.scheduling.annotation.Async;

import java.io.*;
import java.net.Socket;

public class Receiver extends Thread {

    Socket socket;
    DataInputStream dis = null;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;

    public Receiver(Socket socket) {
        this.socket = socket;
    }

    @Async
    //@Override@
    public void run() {

        try {

            dis = new DataInputStream(socket.getInputStream());
            String type = dis.readUTF();

            // type값('file'또는 'msg')을 기준으로 파일이 전송됐는지 문자열이 전송됐는지 구분한다.
            if(type.equals("file")){

                // 전송된 파일 쓰기!
                String result = fileWrite(dis);
                System.out.println("result : " + result);

            }else if(type.equals("msg")){

                // 수신된 메세지 쓰기
                String result = getMsg(dis);
                System.out.println("result : " + result);
            }

            // 클라이언트에 결과 전송 - 먹통이 된다.
            //DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            //dos.writeUTF(result);

        } catch (IOException e) {
            System.out.println("run() Fail!");
            e.printStackTrace();
        } finally {
            try { dis.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private String fileWrite(DataInputStream dis){

        String result;
        String filePath = "D:/request";

        try {
            System.out.println("파일 수신 작업을 시작합니다.");

            // 파일명을 전송 받고 파일명 수정
            String fileNm = dis.readUTF();
            System.out.println("파일명 " + fileNm + "을 전송받았습니다.");

            // 파일을 생성하고 파일에 대한 출력 스트림 생성
            File file = new File(filePath + "/" + fileNm);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            System.out.println(fileNm + "바이트 데이터를 전송받으면서 기록.");

            // 바이트 데이터를 전송받으면서 기록
            int len;
            int size = 4096;
            byte[] data = new byte[size];
            while ((len = dis.read(data)) != -1) {
                bos.write(data, 0, len);
            }

            //bos.flush();
            result = "SUCCESS";

            System.out.println("IP : " + socket.getInetAddress());
            System.out.println("파일 수신 작업을 완료하였습니다.");
            System.out.println("받은 파일의 사이즈 : " + file.length());
        } catch (IOException e) {
            e.printStackTrace();
            result = "ERROR";
        }finally{
            try { bos.close(); } catch (IOException e) { e.printStackTrace(); }
            try { fos.close(); } catch (IOException e) { e.printStackTrace(); }
            try { dis.close(); } catch (IOException e) { e.printStackTrace(); }
        }

        return result;
    }

    private String getMsg(DataInputStream dis){

        String result;

        try {
            System.out.println("파일 수신 작업을 시작합니다.");

            // 파일명을 전송 받고 파일명 수정
            String msg = dis.readUTF();
            System.out.println("msg : " + msg);

            result = "SUCCESS";

            System.out.println("메세지 수신 작업을 완료하였습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            result = "ERROR";
        }finally{
            try { dis.close(); } catch (IOException e) { e.printStackTrace(); }
        }

        return result;
    }
}