package com.dsu.noncloud.Controller;

import com.dsu.noncloud.utils.FileSender;
import com.dsu.noncloud.utils.Receiver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("api")
public class socketController {

    Socket socket;
    ServerSocket serverSocket;
    String serverIp = "192.168.96.135";
    DataInputStream dis;
    String result;

    boolean yn;

    // 클라이언트
    @GetMapping("")
    public String main() {

        try {
            //서버 IP와 포트로 연결
            socket = new Socket(serverIp, 7777);

//            // InputStram
//            InputStream in = socket.getInputStream();
//            dis = new DataInputStream(in);
//
//            result = "Message :" + dis.readUTF();

//            // 데이터 출력
//            System.out.println(result);
//
//            dis.close();

            // 파일 전송용 클래스
            String filePath = "D:";
            String fileNm = "hi.txt";
            //String fileNm = "������ȣ���.hwp";
            FileSender fs = new FileSender(socket, filePath, fileNm);
            fs.start();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 스트림 및 소켓 종료
            System.out.println("연결 종료.");
            return result;
        }


    }

    // 서버
    @GetMapping("admin")
    public String waitClient() throws IOException {
        yn = true;
        try {
            serverSocket = new ServerSocket(7777);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (yn) {
            try {
                Socket socket = serverSocket.accept();

                System.out.println(getTime() + "연결 요청 :" + socket.getInetAddress());

//                // OutputStream
//                OutputStream out = socket.getOutputStream();
//                DataOutputStream dos = new DataOutputStream(out);
//
//
//                dos.writeUTF("Test Message");
//                System.out.println(getTime() + "데이터 전송");

                // 파일 수신용 클래스 생성 및 시작
                Receiver receiver = new Receiver(socket);
                receiver.start();

                yn = false;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                stop(socket, serverSocket);
            }
        }
        return "send success";
    }

    public String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
        return f.format(new Date());
    }

    public void stop(Socket socket, ServerSocket serverSocket) throws IOException {
        serverSocket.close();
        socket.close();
    }

}
