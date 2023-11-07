package xyz.linyh.messageservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.linyh.messageservice.entitys.Message;
import xyz.linyh.messageservice.service.impl.MessageServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootTest
class MessageServiceApplicationTests {

    @Test
    public void test(){

    }


    public static void main(String[] args) {
//        ArrayList<Integer> integers = new ArrayList<>();
//        integers.add(1);
//        integers.add(2);
//        integers.add(3);
//        CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
//        for (Integer integer : integers) {
//            if(integer==2){
//                integers.add(4);
//            }
//        }
        CopyOnWriteArrayList<Integer> integers = new CopyOnWriteArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);

        for(Integer integer : integers) {
            if (integer == 2) {
                integers.add(4);
            }
        }
    }

}
