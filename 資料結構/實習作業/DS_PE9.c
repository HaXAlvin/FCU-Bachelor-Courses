#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct Node *NodePointer;
typedef struct Node{
    int vertex;//連去哪一個head
    NodePointer link;
}Node;
typedef struct {
    int count;//先行幾個
    char name[20];
    int delay;//他延伸出去的delay時間
    NodePointer link;
}HeadNodes;
typedef struct IC{
    char name[20];
    int inputPorts;
    int delayTime;
}IC;

void CircuitPath(HeadNodes heads[],HeadNodes *now,const int *maxPath,char nowOut[],int nowCount,FILE *fw_A){
    Node *ptr=now->link;
    nowCount+=now->delay;
    char newPut[200]={0};
    strcat(newPut,nowOut);
    strcat(newPut,now->name);
//    strcat(nowOut,now->name);
    if(nowCount == *maxPath){
        printf("%s\n",newPut);
        fprintf(fw_A,"%s\n",newPut);
    }else{
        strcat(newPut," ");
    }
    while(ptr){
        CircuitPath(heads,&heads[ptr->vertex],maxPath,newPut,nowCount,fw_A);
        ptr = ptr->link;
    }

}

void countPath(int *maxPath,int nowCount,HeadNodes heads[],HeadNodes *now){
    nowCount+=now->delay;
    if(nowCount > *maxPath){
        *maxPath = nowCount;
    }
    printf("%sX%d ",now->name,nowCount);
    Node *ptr=now->link;
    while(ptr){
        countPath(maxPath,nowCount,heads,&heads[ptr->vertex]);
        ptr = ptr->link;
    }
}

int main(){
    const int Inputs,Outputs,Gates,Types;
    FILE *fr_G,*fr_C,*fw_A;
    fr_G = fopen("Gates.txt","r");
    fr_C = fopen("Circuit.txt","r");
    fw_A = fopen("Output.txt","w");
    int i,j,k,m,n;
    //Gates.txt
    fscanf(fr_G,"%d",&Types);//gate種類數
    IC ICs[Types];
    for(i=0;i<Types;i++){
        fscanf(fr_G,"%s %d %d",ICs[i].name,&ICs[i].inputPorts,&ICs[i].delayTime);
    }
    //Circuit.txt
    fscanf(fr_C,"%d %d %d",&Inputs,&Outputs,&Gates);

    HeadNodes head[Inputs+Outputs+Gates];
    memset(head,0,sizeof(head));

    for(i=0;i<Inputs;i++){
        fscanf(fr_C,"%s",head[i].name);
        head[i].delay=0;
        head[i].link=NULL;
        head[i].count=0;
    }

    for(;i<Inputs+Outputs;i++){
        fscanf(fr_C,"%s",head[i].name);
        head[i].delay=0;
        head[i].link=NULL;
        head[i].count=0;
    }

    for(n=0;i<Inputs+Outputs+Gates&&n<Gates;i++,n++){//新節點
        char ICName[20] = {0},port[20]={0};
        fscanf(fr_C,"%s %s",head[i].name,ICName);
        int now=i,alreadyHave=0,notYetHave=1;
        for(j=0;j<i;j++){//此輸出是不是已存在
            if(!strcmp(head[i].name,head[j].name)){
                now = j;
                alreadyHave=1;
                printf("%sno\n",head[i].name);
                break;
            }
        }
        for(j=0;j<Types;j++){//找是哪一種gate
            if(!strcmp(ICName,ICs[j].name)){
                head[now].delay = ICs[j].delayTime;
                for(k=0;k<ICs[j].inputPorts;k++){//此gate有幾個輸入
                    fscanf(fr_C,"%s",port);//誰輸入的
                    notYetHave = 1;
                    for(m=0;m<i;m++){//這些輸入從哪來
                        if(!strcmp(port,head[m].name)){
                            head[m].count++;
                            Node *ptr = head[m].link;
                            Node *newNode = (Node*)malloc(sizeof(Node));
                            newNode->link=NULL;
                            newNode->vertex = now;//m後連著now
                            if(ptr == NULL){
                                head[m].link = newNode;
                            }else{
                                while(ptr->link){
                                    ptr = ptr->link;
                                }
                                ptr->link = newNode;
                            }
                            notYetHave=0;
                            break;
                        }
                    }
                    if(notYetHave){//找不到gate
                        i++;
                        strcpy(head[i].name,port);
                        Node *newNode = (Node*)malloc(sizeof(Node));
                        newNode->link=NULL;
                        newNode->vertex = now;//新建head後連著now
                        head[i].link = newNode;
                        head[i].count++;


                    }
                }
                break;
            }
        }
        if(alreadyHave){
            i--;
        }

    }
    int maxPath=0;
    for(i=0;i<Inputs;i++){
        countPath(&maxPath,0,head,&head[i]);
        puts("");
    }
    printf("\nmax:%d\n",maxPath);
    fprintf(fw_A,"%d\n",maxPath);
    for(i=0;i<Inputs;i++){
        CircuitPath(head,&head[i],&maxPath,"",0,fw_A);
    }

}
/*
3
NOT 1 1
NOR 2 1
NAND 2 2
4 2 6
x2 x3 x4 x5
z1 z2
A NOR x2 x3
B NAND x3 x4
C NOR x5 A
z1 NAND x2 C
D NOR B C
z2 NOT D
*/
