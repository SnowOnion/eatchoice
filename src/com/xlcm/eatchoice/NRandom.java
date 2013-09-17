package com.xlcm.eatchoice;

import java.util.Random;

/**
 * ����������
 * @author noam 
 */
public class NRandom {

    /**
     * �Ը�����Ŀ����0��ʼ����Ϊ1���������н�������
     * @param no ������Ŀ
     * @return ����������
     */
    public static int[] getSequence(int no) {
        int[] sequence = new int[no];
        for(int i = 0; i < no; i++){
            sequence[i] = i;
        }
        Random random = new Random();
        for(int i = 0; i < no; i++){
            int p = random.nextInt(no);
            int tmp = sequence[i];
            sequence[i] = sequence[p];
            sequence[p] = tmp;
        }
        random = null;
        return sequence;
    }
    
}