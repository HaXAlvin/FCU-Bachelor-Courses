#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define MAX 100
#define FMAX 50
//#define FR "002.tst"
//#define FW "002.tst.result"

typedef struct customer{
    char name[FMAX]; //客戶id、增減窗口
    int arrive; //到達時間、增減窗口時間
    int cost; //處理所需時間、增減窗口代號
    struct customer *next;//同一窗口的下一位客戶
}customer;

typedef struct windows{
    customer *head;//1st客戶
    int num;//客戶數
    int working;//0=關 1=正在關 2=開
    int first;//1st客戶離開時間
}windows;

void ADD(customer tmp,int open,windows win[]){//客戶進入佇列
    //建立串列所需記憶體
    customer *NEW = (customer*)malloc(sizeof(customer));
    *NEW=tmp;

    int min=MAX+1,which=-1,i;//同一窗口最大客戶、選擇窗口
    for(i=0;i<open;i++){
        if (win[i].num<min && win[i].working==2){//選擇最少人、仍開放的窗口
            min = win[i].num;
            which = i;
        }
    }

    if(win[which].num==0){//無人窗口
        win[which].head = NEW;
        win[which].first = NEW->arrive + NEW->cost;//直接計算時間
        win[which].num++;

    }else{//已有人的窗口
        //將指標移到最後一位客戶
        customer *ptr=win[which].head;
        for(i=0;i<win[which].num-1;i++){
            ptr = ptr->next;
        }
        ptr->next = NEW;//將客戶排在佇列最後
        win[which].num++;
    }
    printf("ADD %s %d %d %d\n",NEW->name,NEW->arrive,NEW->cost,which);

}

void DEAL(int open,windows win[],int time,FILE *fw){//確認當前狀況
    int i;
    for(i=0;i<open;i++){//確認所有窗口
        if(win[i].first == time){//若1st客戶即將離開
            printf("LEAVE %s %d %d\n",win[i].head->name,time,i);
            fprintf(fw,"%s %d %d\n",win[i].head->name,time,i);
            win[i].num --;
            customer *release = win[i].head;

            if(win[i].working==1){//待關閉窗口改為關閉
                win[i].working = 0;
            }

            if (win[i].num){//有人窗口
                win[i].head = win[i].head->next;//指標移向下一位
                win[i].first = time + win[i].head->cost;//計算新客戶時間
            }else{//無人窗口
                win[i].head = NULL;
                win[i].first = -1;
            }
            if(!release){
                free(release);
            }
        }
    }
}



int main(){
    //開檔
    char fileRead[FMAX]={0};
    scanf("%s",fileRead);
    char fileWrite[FMAX]={0};
    strcpy(fileWrite,fileRead);
    strcat(fileWrite,".result");
    FILE *fr,*fw;
    while(NULL == (fr = fopen(fileRead,"r"))){
        printf("Error opening reading-file\n");
        system("pause");
    }
    while(NULL==(fw = fopen(fileWrite,"w"))){
        printf("Error opening writing-file\n");
        system("pause");
    }

    int time=0,open,end=0,eof=0;//時間、窗口數、程式結束、讀檔結束
    int i,j;
    //窗口數
    fscanf(fr,"%d",&open);
    windows win[open];
    for(i=0;i<open;i++){//窗口初始化
        win[i].head = NULL;
        win[i].working = 2;
        win[i].num = 0;
        win[i].first = -1;
    }

    customer tmp;
    //第一筆資料，放在暫存
    fscanf(fr,"%s %d %d",tmp.name,&tmp.arrive,&tmp.cost);
    while(!end || !eof){//程式未結束、讀檔未結束
        DEAL(open,win,time,fw);//確認客戶是否離開
        printf("\ntime:%d\n",time);
        for(i=0;i<open;i++){//當前窗口狀況
            printf("win[%d],first:%d,working:%d,num:%d\n",i,win[i].first,win[i].working,win[i].num);
        }
        if(time == tmp.arrive && !eof){//因為輸入已有時間排序，所以如果現在時間是暫存的資料抵達時間才讀取下一個客戶
            if(!strcmp(tmp.name,"#")){//判斷暫存是否關閉窗口
                printf("Close win[%d]\n",tmp.cost);
                if(win[tmp.cost].num){//如果窗口有人
                    customer *ptr=win[tmp.cost].head;//宣告一個指標來找第二位以後的客人
                    win[tmp.cost].working = 1;
                    while(ptr->next){//將第二位以後的客人ADD到別的窗口
                        ADD(*(ptr->next),open,win);
                        free(ptr);
                        ptr = ptr->next;
                    }
                    win[tmp.cost].num=1;
                    win[tmp.cost].head->next = NULL;
                }else{//如果窗口沒人
                    win[tmp.cost].working = 0;
                }
            }else if(!strcmp(tmp.name,"@")){//判斷暫存是否開啟窗口
                win[tmp.cost].working = 2;
                printf("Open win[%d]\n",tmp.cost);
            }else{//判斷暫存是否新增客戶
                tmp.next = NULL;
                ADD(tmp,open,win);

            }
            if(fscanf(fr,"%s %d %d",tmp.name,&tmp.arrive,&tmp.cost)==EOF){//讀取一筆新資料，覆蓋暫存
                eof = 1;
            }
        }
        //DEAL(open,win,time,fw);//確認客戶是否離開
        if(eof){
            for(i=0,end=1;i<open;i++){
                if(win[i].num){
                    end=0;
                }
            }
        }
        time++;
    }
    return 0;
}
