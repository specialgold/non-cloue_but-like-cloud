package com.dsu.noncloud.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.dsu.noncloud.utils.*;

@Controller
@RequestMapping("/")
public class SendController {

    @GetMapping("")
    public String main() {

        return "index.html";
    }

    @GetMapping("/send")
    public String send() {
        String serverIp = "192.168.96.135";
        Socket socket = null;

        try {
            // 서버 연결
            socket = new Socket(serverIp, 7777);
            System.out.println("서버에 연결되었습니다.");

            // 파일 전송용 클래스

            String filePath = "D:";
            String fileNm = "hi.txt";
            //String fileNm = "������ȣ���.hwp";
            FileSender fs = new FileSender(socket, filePath, fileNm);
            fs.start();


            // 메시지 전송용 클래스
            /*
            String msg = "메세지 전달~";
            MsgSender ms = new MsgSender(socket, msg);
            ms.start();
            */

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

    @GetMapping("/receive")
    public String receive() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {

            serverSocket = new ServerSocket(7777);
            System.out.println("서버에 시작되었습니다.");

            boolean yn = true;

            // 클라이언트와의 연결 대기 루프
            while( yn ){

                System.out.println("새로운 Client의 연결요청을 기다립니다.");

                // 연결되면 통신용 소켓 생성
                socket = serverSocket.accept();
                System.out.println("클라이언트 서버 주소" + serverSocket.getInetAddress().toString());
                System.out.println("클라이언트와 연결되었습니다.");

                // 파일 수신용 클래스 생성 및 시작
                Receiver receiver = new Receiver(socket);
                receiver.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

}
