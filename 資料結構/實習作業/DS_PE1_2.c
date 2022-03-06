#include <stdio.h>
#include <stdlib.h>

void f(int n){
    int tmp = abs(n%-2);//以變數儲存短除法後的答案
    if (n<0)
        f((n-1)/-2);//以-2為基底，負數需額外減1
    else if (n>0)
        f(n/-2);//如為正數，直接/-2
    else
        return;//遞迴終止條件 n==0
    printf("%d",tmp);//最後輸出，達到反輸出的效果
}

int main(){
    int i,input,times;
    scanf("%d",&times);//輸入測資數量
    for(i=0;i<times;i++){
        scanf("%d",&input);
        if(input==0)//如輸入0直接印出0
            printf("0");
        else//非0則進入遞迴函式
            f(input);
        printf("\n");
    }
}

