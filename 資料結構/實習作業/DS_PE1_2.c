#include <stdio.h>
#include <stdlib.h>

void f(int n){
    int tmp = abs(n%-2);//�H�ܼ��x�s�u���k�᪺����
    if (n<0)
        f((n-1)/-2);//�H-2���򩳡A�t�ƻ��B�~��1
    else if (n>0)
        f(n/-2);//�p�����ơA����/-2
    else
        return;//���j�פ���� n==0
    printf("%d",tmp);//�̫��X�A�F��Ͽ�X���ĪG
}

int main(){
    int i,input,times;
    scanf("%d",&times);//��J����ƶq
    for(i=0;i<times;i++){
        scanf("%d",&input);
        if(input==0)//�p��J0�����L�X0
            printf("0");
        else//�D0�h�i�J���j�禡
            f(input);
        printf("\n");
    }
}

