package kr.or.mrhi.sixclass;




import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DataBaseTest {
   //mysql Driver & URL 선언
   public static final int INSERT = 1,SEARCH = 2,DELETE = 3,UPDATE = 4, SHOWTB = 5, FINISH = 6;
   public static final Scanner scan = new Scanner(System.in);
   public static void main(String[] args) throws IOException {
      boolean flag = false;
      int selectNumber = 0;
      //select menu
      while(!flag) {
         //메뉴출력 및 번호선택
         selectNumber = displayMenu(); //메뉴선택
         
         switch (selectNumber) {
         case INSERT: phoneBookInsert(); break;   //전화번호부 입력
         case SEARCH: phoneBookSearch(); break;   //전화번호부 검색
         case DELETE: phoneBookDelete(); break;   //전화번호부 삭제
         case UPDATE: phoneBookUpdate(); break;   //전화번호부 수정
         case SHOWTB: phoneBookSelect(); break;   //전화번호부 출력
         case FINISH: flag = true; break;
         default: System.out.println("입력가능한 범위를 벗어났습니다."); break;
         }//end of switch
      }//end of while
      System.out.println("프로그램이 종료되었습니다.");
   }//end of main
   //1.전화번호부 입력
   private static void phoneBookInsert() {
      boolean flag = false;
      String phoneNumber = null;
      String name = null;
      String gender = null;
      String job = null;
      String birthday = null;
      int age = 0;
      while(true) {
         System.out.print("전화번호(000-0000-0000)를 입력>>");
         phoneNumber = scan.nextLine();
         if(phoneNumber.length() != 13) {
            System.out.println("전화번호입력 양식을 지켜주세요.");
            continue;
         }
         
         boolean checkPhoneNumber = duplicatePhoneNumberCheck(phoneNumber);
         if(checkPhoneNumber == true) {
            System.out.println("존재하는 폰번호입니다. 다시 입력해주세요.");
            continue;
         }break;
      }//end of while
      
         while(true) {
         System.out.print("이름(김김김) 입력>>");
         name = scan.nextLine();
         if(name.length() > 7 || name.length() < 2) {
            System.out.println("이름입력 양식을 지켜주세요.");
            continue;
         }else {
        while(true) {
         System.out.print("이름(김김김) 입력>>");
         name = scan.nextLine();
         if(name.length() > 7 || name.length() < 2) {
            System.out.println("이름입력 양식을 지켜주세요.");
            continue;
         }else {
            break;
         }
         }      break;
         }
         }//end of while
         while(true) {
            System.out.print("성별(남자,여자) 입력>>");
            gender = scan.nextLine();
            if(!(gender.equals("남자") || gender.equals("여자"))) {
               System.out.println("성별입력 양식을 지켜주세요.");
               continue;
            }else {
               break;
            }
         }//end of while
         while(true) {
            System.out.print("직업(20자 이하) 입력>>");
            job = scan.nextLine();
            if(job.length() >=20 || job.length()<1) {
               System.out.println("직업입력 양식을 지켜주세요.");
               continue;
            }else {
               break;
            }
         }//end of while
         while(true) {
            System.out.print("생년월일(8자리) 입력>>");
            birthday = scan.nextLine();
            if(birthday.length() != 8) {
               System.out.println("생년월일입력 양식을 지켜주세요.");
               continue;
            }else {
               int year = Integer.parseInt(birthday.substring(0,4));
               int currentYear = Calendar.getInstance().get(Calendar.YEAR);
               age = currentYear - year + 1;
               
               break;
            }
         }//end of while
         PhoneBook phoneBook = new PhoneBook(phoneNumber, name, gender, job, birthday, age);
         
         //전화번호부 입력하기
         int count = DBController.phoneBookInsertTBL(phoneBook);
         
         if(count != 0) {
            System.out.println(phoneBook.getName()+"님 삽입 성공");
         }else {
            System.out.println("삽입실패");
         }
   }
   //2.전화번호부 검색
   private static void phoneBookSearch() {
      //검색할 내용 입력받기(검색할 조건을 개개인이 부여하기.)
      final int PHONE = 1, NAME = 2, GENDER = 3, EXIT = 4;
      List<PhoneBook> list = new ArrayList<PhoneBook>();
      boolean flag = false;
      String searchData = null;
      int searchNumber = 0;
      
      while(!flag) {
         
         int selectNumber = displaySearchMenu();
         
         switch(selectNumber) {
         case PHONE: System.out.print("1.전화번호부입력>>");
            searchData = scan.nextLine();
            searchNumber = PHONE;
            break;
         case NAME: System.out.print("2.이름입력>>");
            searchData = scan.nextLine();
            searchNumber = NAME;
            break;
         case GENDER: System.out.print("3.성별입력>>");
            searchData = scan.nextLine();
            searchNumber = GENDER;
            break;
         case EXIT://함수종료
            return;
         default : 
            System.out.println("검색번호 범위를 벗어났습니다.(다시입력요망)");
            continue;
         }
         flag = true;
      }
      list = DBController.phoneBookSearchTBL(searchData, searchNumber);
      if(list.size() <= 0) {
         System.out.println("찾는 데이터가 없거나, 검색범위 오류발생");
         return;
      }
      for(PhoneBook pb:list) {
         System.out.println(pb);
      }
   }
   //3.전화번호부 삭제
   private static void phoneBookDelete() {
      System.out.print("삭제할 이름 입력>>");
      String name = scan.nextLine();
      
      int count = DBController.phoneBookDeleteTBL(name);
      if(count != 0) {
         System.out.println(name + "님이 삭제가 완료되었습니다.");
      }else {
         System.out.println(name + "님이 삭제되지 않았습니다.");
      }
   }
   //4.전화번호부 수정
   private static void phoneBookUpdate() {
      System.out.print("수정할사람의 전화번호를 입력해주세요>>");
      String phoneNumber = scan.nextLine();
      
      System.out.print("수정할이름을 입력해주세요>>");
      String name = scan.nextLine();
      
      int count = DBController.phoneBookUpdateTBL(phoneNumber, name);
      if(count != 0) {
         System.out.println(phoneNumber + "님 이름이 수정되었습니다.");
      }else {
         System.out.println(phoneNumber + "님 이름이 수정되지 않았습니다.");
      }
   }
   //5.전화번호부 출력
   private static void phoneBookSelect() {
      List<PhoneBook> list = new ArrayList<PhoneBook>();
      
      list = DBController.phoneBookSelectTBL();
      if(list.size() <= 0) {
         System.out.println("출력할 데이터가 없습니다.");
         return;
      }
      for(PhoneBook pb:list) {
         System.out.println(pb.toString());
      }
   }
   //메뉴출력 및 번호선택
   private static int displayMenu() {
      int selectNumber = 0;
      boolean flag = false;
      while(!flag) {
         
         System.out.println("----------------------------------");
         System.out.println("1.입력 2.조회 3.삭제 4.수정 5.출력 6.종료");
         System.out.println("----------------------------------");
         System.out.println("번호선택>>");
         try {
            selectNumber = Integer.parseInt(scan.nextLine());
         }catch(InputMismatchException e){
            System.out.println("숫자만 입력해주세요.");
//         scan.next(); //버퍼 삭제
            continue;
         }catch(Exception e) {
            System.out.println("문제발생, 다시 입력해주세요.");
//         scan.next(); //버퍼 삭제
            continue;
      }
         break;
      }
      return selectNumber;
   }
   //
   private static int displaySearchMenu() {
      int selectNumber = 0;
      boolean flag = false;
      while(!flag) {
         
         System.out.println("---------------------------------------------");
         System.out.println("검색선택 1.전화번호부검색 2.이름검색 3.성별검색 4.검색나가기");
         System.out.println("---------------------------------------------");
         System.out.println("번호선택>>");
         try {
            selectNumber = Integer.parseInt(scan.nextLine());
         }catch(InputMismatchException e){
            System.out.println("숫자만 입력해주세요.");
//         scan.next(); //버퍼 삭제
            continue;
         }catch(Exception e) {
            System.out.println("문제발생, 다시 입력해주세요.");
//         scan.next(); //버퍼 삭제
            continue;
      }
         break;
      }
      return selectNumber;
   }

   //2.전화번호부 검색
      private static boolean duplicatePhoneNumberCheck(String phoneNumber) {
         final int PHONE = 1;
         List<PhoneBook> list = new ArrayList<PhoneBook>();
         int searchNumber = PHONE;
         
         list = DBController.phoneBookSearchTBL(phoneNumber, searchNumber);
         if(list.size() >= 1 ) {
            return true;
         }
         return false;
      }
}