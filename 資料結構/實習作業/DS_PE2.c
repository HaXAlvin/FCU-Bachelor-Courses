#include <stdio.h>
#include <stdlib.h>
#include <time.h>//rand
#include <string.h>//memset
#define MAX_I 40//陣列MAX與MIN
#define MAX_J 20
#define MIN_I 2
#define MIN_J 2

typedef struct{
    int i;
    int j;
}direction;//紀錄i,j的結構

int Move(direction move,direction array,direction *start){//移動的函式
    printf("%d %d\n",start->i+move.i,start->j+move.j);
    if((start->j+move.j <= array.j-1) && (start->j+move.j >= 0) && (start->i+move.i >= 0) && (start->i+move.i <= array.i-1)){//判斷是否超出範圍
        start->i+=move.i;//移動
        start->j+=move.j;
        return 0;//MoveIsNo=0
    }else{//不合法的移動
        printf("Move not arrow\n");
        return 1;//MoveIsNo=1
    }
}



int main()
{

    direction array,start,way[8]={{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1},{-1,0}};//定義0~7所代表的方向
    int i,j,randMove,moveIsNo,newPlace=1;//newPlace紀錄第幾個新位置
    FILE *fp;
    srand(time(NULL));
    while(!(fp = fopen("D0745378_PE2.csv","w"))){//開檔
        printf("Error opening file!\n");
        printf("Please close the file and try again.\n");
        system("pause");
    }
    while(1){//輸入陣列大小
        printf("Input array size: ");
        scanf("%d %d",&array.i,&array.j);
        if((array.i>MAX_I)||(array.i<=MIN_I)||(array.j>MAX_J)||(array.j<=MIN_J)){
            printf("Too large!\n");
        }else{
            break;
        }
    }
    while(1){//輸入起始點
        printf("Input start point: ");
        scanf("%d %d",&start.i,&start.j);//0~array.i-1 0~array.j-1
        if(start.i>=array.i || start.i<0 || start.j>=array.j || start.j<0){
            printf("Out of range!\n");
        }else{
            break;
        }
    }

    int count[array.i][array.j];//宣告陣列紀錄經過幾次
    memset(count,0,array.i*array.j*sizeof(int));//初始化


    count[start.i][start.j]++;//起始點+1

    while(newPlace!=array.i*array.j){
        randMove=rand()%8;//randMove 0~7
        moveIsNo=0;//預設下一步合法

        if(moveIsNo = Move(way[randMove],array,&start)){//判斷下一步是否合法
            continue;//不合法則重新亂數
        }
        count[start.i][start.j]++;
        if (count[start.i][start.j]==1){//紀錄是否第一次經過
            newPlace++;
        }

    }//end while

    int total=0;//計算總計
    for(i=0;i<array.i;i++){
        for(j=0;j<array.j;j++){
            total+=count[i][j];
        }
    }


    fprintf(fp,"Total number of legal moves: %d\n",total);//開始輸出
    printf("\nTotal number of legal moves: %d\n",total);
    for(i=0;i<array.i;i++){
        for(j=0;j<array.j;j++){
            fprintf(fp,"%d,",count[i][j]);//','為分格
            printf("%-5d",count[i][j]);
        }
        fprintf(fp,"\n");//'\n'為分行
        puts("");
    }

    fclose(fp);
    return 0;
}
