#include <stdio.h>
#include <stdlib.h>
#include <time.h>//rand
#include <string.h>//memset
#define MAX_I 40//�}�CMAX�PMIN
#define MAX_J 20
#define MIN_I 2
#define MIN_J 2

typedef struct{
    int i;
    int j;
}direction;//����i,j�����c

int Move(direction move,direction array,direction *start){//���ʪ��禡
    printf("%d %d\n",start->i+move.i,start->j+move.j);
    if((start->j+move.j <= array.j-1) && (start->j+move.j >= 0) && (start->i+move.i >= 0) && (start->i+move.i <= array.i-1)){//�P�_�O�_�W�X�d��
        start->i+=move.i;//����
        start->j+=move.j;
        return 0;//MoveIsNo=0
    }else{//���X�k������
        printf("Move not arrow\n");
        return 1;//MoveIsNo=1
    }
}



int main()
{

    direction array,start,way[8]={{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1},{-1,0}};//�w�q0~7�ҥN����V
    int i,j,randMove,moveIsNo,newPlace=1;//newPlace�����ĴX�ӷs��m
    FILE *fp;
    srand(time(NULL));
    while(!(fp = fopen("D0745378_PE2.csv","w"))){//�}��
        printf("Error opening file!\n");
        printf("Please close the file and try again.\n");
        system("pause");
    }
    while(1){//��J�}�C�j�p
        printf("Input array size: ");
        scanf("%d %d",&array.i,&array.j);
        if((array.i>MAX_I)||(array.i<=MIN_I)||(array.j>MAX_J)||(array.j<=MIN_J)){
            printf("Too large!\n");
        }else{
            break;
        }
    }
    while(1){//��J�_�l�I
        printf("Input start point: ");
        scanf("%d %d",&start.i,&start.j);//0~array.i-1 0~array.j-1
        if(start.i>=array.i || start.i<0 || start.j>=array.j || start.j<0){
            printf("Out of range!\n");
        }else{
            break;
        }
    }

    int count[array.i][array.j];//�ŧi�}�C�����g�L�X��
    memset(count,0,array.i*array.j*sizeof(int));//��l��


    count[start.i][start.j]++;//�_�l�I+1

    while(newPlace!=array.i*array.j){
        randMove=rand()%8;//randMove 0~7
        moveIsNo=0;//�w�]�U�@�B�X�k

        if(moveIsNo = Move(way[randMove],array,&start)){//�P�_�U�@�B�O�_�X�k
            continue;//���X�k�h���s�ü�
        }
        count[start.i][start.j]++;
        if (count[start.i][start.j]==1){//�����O�_�Ĥ@���g�L
            newPlace++;
        }

    }//end while

    int total=0;//�p���`�p
    for(i=0;i<array.i;i++){
        for(j=0;j<array.j;j++){
            total+=count[i][j];
        }
    }


    fprintf(fp,"Total number of legal moves: %d\n",total);//�}�l��X
    printf("\nTotal number of legal moves: %d\n",total);
    for(i=0;i<array.i;i++){
        for(j=0;j<array.j;j++){
            fprintf(fp,"%d,",count[i][j]);//','������
            printf("%-5d",count[i][j]);
        }
        fprintf(fp,"\n");//'\n'������
        puts("");
    }

    fclose(fp);
    return 0;
}
