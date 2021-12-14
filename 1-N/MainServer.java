package socket;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MainServer {

    int port = 7777; // 정수변수 port 선언 후 7777 대입
    ServerSocket server = null; // ServerSocket 타입 server 선언 후 null 대입
    Socket child = null; // Socket type child 선언 후 null 대입

    HashMap<String, PrintWriter> hm; // 컬렉션인 HashMap 타입의 key 값을 String 값은 PrintWriter 인 hm 변수 선언

    public MainServer() { // ChatServer 생성자

        ChatSverThread sr; // ChatServerThread 타입에 sr 변수 선언, 브로드 캐스팅을 하기위한 쓰레드 객체
        Thread t; // Thread 타입의 t 변수 선언

        try {
            server = new ServerSocket(port); // 서버소켓을 생성해서 server 변수에 대입

            System.out.println( "**************************************" );//출력
            System.out.println( "* 채팅 서버 *" ); // 출력
            System.out.println( "**************************************" );//출력
            System.out.println( "클라이언트의 접속을 기다립니다." );//출력


            hm = new HashMap<String, PrintWriter>(); // hashMap 객체를 생성해서 hm 변수에 대입

            while(true) {
                /*
                ServerSocket의 변수인 server를 이용하여 accept함수 호출을 하여
                클라이언트 접속시까지 대기
                접속시 클라이언트와 연결
                클라이언트의 소켓을 연결 받음
                 */
                child = server.accept();

                /*
                Socket type에 변수인 child가 null 값이 아니면 실행
                child에는 클라이언트 소켓과 연결을 할 수 있는 소켓
                 */
                if(child != null) {
                    /*
                    ChatSverThread 객체를 Socket과 HashMap을 받아서 생성 후
                    ChatSverThread의 변수인 sr에 대입
                     */
                    sr = new ChatSverThread(child, hm);
                    /*
                    Thread 객체를 ChatSverThread을 받아서 생성 후
                    Thread의 변수인 t에 대입
                     */
                    t = new Thread(sr);
                    // Thread 시작
                    t.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외처리 출력
        }

    }
    
    public static void main(String[] args) {
        new MainServer(); // MainServer 객체 생성
    }


}
