#include <stdio.h>
#include <stdlib.h>

int f(int n){ //計算n!函式
    if (n==1) return 1; //遞迴結束條件
    return n*f(n-1);  //n!=n*(n-1)!
}


int main(){
    int input;
    while(scanf("%d",&input)==1){ //非數字則結束迴圈
        if((input<1) || (input>10)){//限制輸入範圍
            printf("Error\n");
            continue;//重新迴圈
        }
        printf("%d\n",f(input));
    }
}
