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
   //mysql Driver & URL ����
   public static final int INSERT = 1,SEARCH = 2,DELETE = 3,UPDATE = 4, SHOWTB = 5, FINISH = 6;
   public static final Scanner scan = new Scanner(System.in);
   public static void main(String[] args) throws IOException {
      boolean flag = false;
      int selectNumber = 0;
      //select menu
      while(!flag) {
         //�޴���� �� ��ȣ����
         selectNumber = displayMenu(); //�޴�����
         
         switch (selectNumber) {
         case INSERT: phoneBookInsert(); break;   //��ȭ��ȣ�� �Է�
         case SEARCH: phoneBookSearch(); break;   //��ȭ��ȣ�� �˻�
         case DELETE: phoneBookDelete(); break;   //��ȭ��ȣ�� ����
         case UPDATE: phoneBookUpdate(); break;   //��ȭ��ȣ�� ����
         case SHOWTB: phoneBookSelect(); break;   //��ȭ��ȣ�� ���
         case FINISH: flag = true; break;
         default: System.out.println("�Է°����� ������ ������ϴ�."); break;
         }//end of switch
      }//end of while
      System.out.println("���α׷��� ����Ǿ����ϴ�.");
   }//end of main
   //1.��ȭ��ȣ�� �Է�
   private static void phoneBookInsert() {
      boolean flag = false;
      String phoneNumber = null;
      String name = null;
      String gender = null;
      String job = null;
      String birthday = null;
      int age = 0;
      while(true) {
         System.out.print("��ȭ��ȣ(000-0000-0000)�� �Է�>>");
         phoneNumber = scan.nextLine();
         if(phoneNumber.length() != 13) {
            System.out.println("��ȭ��ȣ�Է� ����� �����ּ���.");
            continue;
         }
         
         boolean checkPhoneNumber = duplicatePhoneNumberCheck(phoneNumber);
         if(checkPhoneNumber == true) {
            System.out.println("�����ϴ� ����ȣ�Դϴ�. �ٽ� �Է����ּ���.");
            continue;
         }break;
      }//end of while
      
         while(true) {
         System.out.print("�̸�(����) �Է�>>");
         name = scan.nextLine();
         if(name.length() > 7 || name.length() < 2) {
            System.out.println("�̸��Է� ����� �����ּ���.");
            continue;
         }else {
        while(true) {
         System.out.print("�̸�(����) �Է�>>");
         name = scan.nextLine();
         if(name.length() > 7 || name.length() < 2) {
            System.out.println("�̸��Է� ����� �����ּ���.");
            continue;
         }else {
            break;
         }
         }      break;
         }
         }//end of while
         while(true) {
            System.out.print("����(����,����) �Է�>>");
            gender = scan.nextLine();
            if(!(gender.equals("����") || gender.equals("����"))) {
               System.out.println("�����Է� ����� �����ּ���.");
               continue;
            }else {
               break;
            }
         }//end of while
         while(true) {
            System.out.print("����(20�� ����) �Է�>>");
            job = scan.nextLine();
            if(job.length() >=20 || job.length()<1) {
               System.out.println("�����Է� ����� �����ּ���.");
               continue;
            }else {
               break;
            }
         }//end of while
         while(true) {
            System.out.print("�������(8�ڸ�) �Է�>>");
            birthday = scan.nextLine();
            if(birthday.length() != 8) {
               System.out.println("��������Է� ����� �����ּ���.");
               continue;
            }else {
               int year = Integer.parseInt(birthday.substring(0,4));
               int currentYear = Calendar.getInstance().get(Calendar.YEAR);
               age = currentYear - year + 1;
               
               break;
            }
         }//end of while
         PhoneBook phoneBook = new PhoneBook(phoneNumber, name, gender, job, birthday, age);
         
         //��ȭ��ȣ�� �Է��ϱ�
         int count = DBController.phoneBookInsertTBL(phoneBook);
         
         if(count != 0) {
            System.out.println(phoneBook.getName()+"�� ���� ����");
         }else {
            System.out.println("���Խ���");
         }
   }
   //2.��ȭ��ȣ�� �˻�
   private static void phoneBookSearch() {
      //�˻��� ���� �Է¹ޱ�(�˻��� ������ �������� �ο��ϱ�.)
      final int PHONE = 1, NAME = 2, GENDER = 3, EXIT = 4;
      List<PhoneBook> list = new ArrayList<PhoneBook>();
      boolean flag = false;
      String searchData = null;
      int searchNumber = 0;
      
      while(!flag) {
         
         int selectNumber = displaySearchMenu();
         
         switch(selectNumber) {
         case PHONE: System.out.print("1.��ȭ��ȣ���Է�>>");
            searchData = scan.nextLine();
            searchNumber = PHONE;
            break;
         case NAME: System.out.print("2.�̸��Է�>>");
            searchData = scan.nextLine();
            searchNumber = NAME;
            break;
         case GENDER: System.out.print("3.�����Է�>>");
            searchData = scan.nextLine();
            searchNumber = GENDER;
            break;
         case EXIT://�Լ�����
            return;
         default : 
            System.out.println("�˻���ȣ ������ ������ϴ�.(�ٽ��Է¿��)");
            continue;
         }
         flag = true;
      }
      list = DBController.phoneBookSearchTBL(searchData, searchNumber);
      if(list.size() <= 0) {
         System.out.println("ã�� �����Ͱ� ���ų�, �˻����� �����߻�");
         return;
      }
      for(PhoneBook pb:list) {
         System.out.println(pb);
      }
   }
   //3.��ȭ��ȣ�� ����
   private static void phoneBookDelete() {
      System.out.print("������ �̸� �Է�>>");
      String name = scan.nextLine();
      
      int count = DBController.phoneBookDeleteTBL(name);
      if(count != 0) {
         System.out.println(name + "���� ������ �Ϸ�Ǿ����ϴ�.");
      }else {
         System.out.println(name + "���� �������� �ʾҽ��ϴ�.");
      }
   }
   //4.��ȭ��ȣ�� ����
   private static void phoneBookUpdate() {
      System.out.print("�����һ���� ��ȭ��ȣ�� �Է����ּ���>>");
      String phoneNumber = scan.nextLine();
      
      System.out.print("�������̸��� �Է����ּ���>>");
      String name = scan.nextLine();
      
      int count = DBController.phoneBookUpdateTBL(phoneNumber, name);
      if(count != 0) {
         System.out.println(phoneNumber + "�� �̸��� �����Ǿ����ϴ�.");
      }else {
         System.out.println(phoneNumber + "�� �̸��� �������� �ʾҽ��ϴ�.");
      }
   }
   //5.��ȭ��ȣ�� ���
   private static void phoneBookSelect() {
      List<PhoneBook> list = new ArrayList<PhoneBook>();
      
      list = DBController.phoneBookSelectTBL();
      if(list.size() <= 0) {
         System.out.println("����� �����Ͱ� �����ϴ�.");
         return;
      }
      for(PhoneBook pb:list) {
         System.out.println(pb.toString());
      }
   }
   //�޴���� �� ��ȣ����
   private static int displayMenu() {
      int selectNumber = 0;
      boolean flag = false;
      while(!flag) {
         
         System.out.println("----------------------------------");
         System.out.println("1.�Է� 2.��ȸ 3.���� 4.���� 5.��� 6.����");
         System.out.println("----------------------------------");
         System.out.println("��ȣ����>>");
         try {
            selectNumber = Integer.parseInt(scan.nextLine());
         }catch(InputMismatchException e){
            System.out.println("���ڸ� �Է����ּ���.");
//         scan.next(); //���� ����
            continue;
         }catch(Exception e) {
            System.out.println("�����߻�, �ٽ� �Է����ּ���.");
//         scan.next(); //���� ����
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
         System.out.println("�˻����� 1.��ȭ��ȣ�ΰ˻� 2.�̸��˻� 3.�����˻� 4.�˻�������");
         System.out.println("---------------------------------------------");
         System.out.println("��ȣ����>>");
         try {
            selectNumber = Integer.parseInt(scan.nextLine());
         }catch(InputMismatchException e){
            System.out.println("���ڸ� �Է����ּ���.");
//         scan.next(); //���� ����
            continue;
         }catch(Exception e) {
            System.out.println("�����߻�, �ٽ� �Է����ּ���.");
//         scan.next(); //���� ����
            continue;
      }
         break;
      }
      return selectNumber;
   }

   //2.��ȭ��ȣ�� �˻�
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