#include <stdio.h>
#include <stdlib.h>

int f(int n){ //�p��n!�禡
    if (n==1) return 1; //���j��������
    return n*f(n-1);  //n!=n*(n-1)!
}


int main(){
    int input;
    while(scanf("%d",&input)==1){ //�D�Ʀr�h�����j��
        if((input<1) || (input>10)){//�����J�d��
            printf("Error\n");
            continue;//���s�j��
        }
        printf("%d\n",f(input));
    }
}
