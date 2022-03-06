#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#define MAXSeller 10
#define MAXName 20

typedef struct Data {
    char seller[MAXName];//賣家
    int cost;//商品價格
} Data;

typedef struct Tree {
    char selling[MAXName];//商品名
    int amount;//商品數
    Data info[MAXSeller];//min heap
    struct Tree *Left,*Right;
} Tree;

Tree* MALLOC() {//tree初始化
    int i;
    Tree *tmp = (Tree*)malloc(sizeof(Tree));
    if(!tmp)
        exit(EXIT_FAILURE);
    for(i=0; i<MAXSeller; i++) {
        tmp->info[i].cost=-1;
        strcpy(tmp->info[i].seller,"");
    }
    tmp->amount=-1;
    tmp->Left=NULL;
    tmp->Right=NULL;
    strcpy(tmp->selling,"");
    return tmp;
}

FILE* FOPEN(char *name,char *way){
    FILE *tmp = fopen(name,way);
    if(!tmp)
        exit(EXIT_FAILURE);
    else
        return tmp;
}

char* TOLOWER(char *tmp){//回傳小寫版字串
    int i;
    char *output = malloc(sizeof(tmp));
    strcpy(output,tmp);
    for(i=0;i<strlen(output);i++){
        output[i] = tolower(output[i]);
    }
    return output;
}

int STRCMP(char *a,char *b){//不看大小寫的strcmp
    return strcmp(TOLOWER(a),TOLOWER(b));
}

void sort_heap(Data heap[],int cost,char *seller,int *amount) {//加入新的seller 同時排序heap
    int i = ++(*amount);//同商品數量++
    while(i!=1&&cost<heap[i/2].cost) {//將cost高的往下放
        heap[i].cost = heap[i/2].cost;
        strcpy(heap[i].seller,heap[i/2].seller);
        i=i/2;
    }
    heap[i].cost=cost;
    strcpy(heap[i].seller,seller);
}

Tree* search(char productName[],Tree *products,int *have_same) { //英文 左小 右大
    printf("---search---\n");
    Tree *ptr = products,*pre=NULL;//ptr存目標位址 pre存目標的爸爸
    while(ptr) { //not null
        int ret = STRCMP(productName,ptr->selling);
        pre = ptr;
        if(ret<0) {
//            printf("toLeft\n");
            ptr = ptr->Left;
        } else if(ret>0) {
//            printf("toRight\n");
            ptr = ptr->Right;
        } else if (ret==0) {
//            printf("find\n");
            *have_same = 1;
            return ptr ;//find
        }
    }
    return pre;//如果找不到就回傳應放位置的爸爸
}

void inorder(Tree *ptr,int counting[][5],int way,FILE *fptr) {//way=0僅運算 way=1運算+輸出
    static int height=1;//計算最大高度
    if(!ptr){
        counting[0][4] = 0;
        return;
    }
    if(ptr->Left) {
        height++;
        inorder(ptr->Left,counting,way,fptr);
        if(height > counting[0][4]){
            counting[0][4] = height;
            printf("height:%d\n",counting[0][4]);

        }
        height--;
    }
    if(ptr) {
        counting[0][3]++;
        if(way){
            printf("%s\n",ptr->selling);
            fprintf(fptr,"%s\n",ptr->selling);
        }
    }
    if(ptr->Right) {
        height++;
        inorder(ptr->Right,counting,way,fptr);
        if(height > counting[0][4]){
            counting[0][4] = height;
        }
        height--;
    }
    if(height > counting[0][4]){
        counting[0][4] = height;
    }
}

Tree* insert(Tree *products,FILE *fptr) {//新增資料
    printf("---insert---\n");
    int cost,have_same;
    Tree *ptr=NULL;
    char productName[MAXName]= {0},seller[MAXName]= {0};
    fscanf(fptr,"%s %s %d",productName,seller,&cost);

    if(!products) {//first Node
        printf("first\n");
        products = MALLOC();
        products->amount = 1;
        strcpy(products->selling,productName);
        products->info[1].cost = cost;//heap從1開始
        strcpy(products->info[1].seller,seller);

    } else { //other node
        have_same=0;//有無同商品
        ptr = search(productName,products,&have_same); //呼叫search找同商品或應新增node的位置
        if(have_same) { //有同商品
            sort_heap(ptr->info,cost,seller,&(ptr->amount));//呼叫sort_heap新增資料進heap
            printf("ADD Same:%s\n",ptr->selling);
        } else { //新商品
            Tree *tmp = MALLOC();
            tmp->amount = 1;
            strcpy(tmp->selling,productName);
            tmp->info[1].cost = cost;
            strcpy(tmp->info[1].seller,seller);
            if(STRCMP(productName,ptr->selling)<0) {//判斷應放在爸爸的左子還是右子
                ptr->Left = tmp;
                printf("ADD NEW LEFT:%s\n",ptr->Left->selling);
            } else {
                ptr->Right = tmp;
                printf("ADD NEW RIGHT:%s\n",ptr->Right->selling);
            }

        }
    }
    return products;
}

void prod_search(Tree *products,int counting[][5],FILE *fptr,FILE *read){//product search功能
    printf("---prod---\n");
    char productName[MAXName]={0};
    int have_same=0;//是否有此商品
    fscanf(read,"%s",productName);
    Tree *ptr = search(productName,products,&have_same);
    if(have_same){//有此商品
        printf("%s\n",ptr->selling);
        fprintf(fptr,"%s\n",ptr->selling);
        for(int i=1;i<=ptr->amount;i++){
            printf("%s %d\n",ptr->info[i].seller,ptr->info[i].cost);
            fprintf(fptr,"%s %d\n",ptr->info[i].seller,ptr->info[i].cost);
        }
        printf("------------------------------\n");
        fprintf(fptr,"------------------------------\n");
    }else{//無此商品
        counting[1][1]++;
        printf("%s doesn’t exist!\n",productName);
        fprintf(fptr,"%s doesn’t exist!\n",productName);
    }
}

void del_min(Data info[],int *amount){//移除cost最小的heap
    printf("---del_min---\n");
    int min = info[1].cost;//最小值
    Data last = info[(*amount)--];//最後一個兒子
    int parent=1,child=2;
    while(child <= *amount)
    {
        if(child<=*amount && info[child].cost > info[child+1].cost)//找小的左子或右子
            child++;
        if(min > info[child].cost)//找到新的最小
            break;
        //移到下一層
        info[parent] = info[child];
        parent = child;
        child*=2;
    }
    info[parent] = last;//將最後一個往上換
    while(parent!=1)//確定順序無誤
    {
        if(info[parent].cost<info[parent/2].cost)
        {
            Data tmp = info[parent];
            info[parent] = info[parent/2];
            info[parent/2] = tmp;
        }
        parent/=2;
    }

}

Tree* del_BST(Tree *products,char *need) {//刪除tree節點
    printf("---del---\n");
    Tree *pre = NULL,*left_max=NULL,*right_min=NULL,*ptr = products;
    if(!products) {
        printf("tree is empty\n");
        return (Tree*)NULL;
    }
    while(ptr) {//ptr=目標node，pre=目標node的爸爸
        if(STRCMP(need,ptr->selling)>0) {
            if(!ptr->Right) {//no such node
                printf("no found\n");
                return products;
            } else {
                pre = ptr;
                ptr = ptr->Right;
            }
        } else if (STRCMP(need,ptr->selling)<0) {
            if(!ptr->Left) {//no such node
                printf("no found\n");
                return products;
            } else {
                pre = ptr;
                ptr = ptr->Left;
            }
        } else if (!STRCMP(need,ptr->selling)) {
            break;//found
        }
    }

    //pre=NULL代表刪除的node是頭
    if(!pre) {
        Tree *tmp = NULL;//tmp=目標的(左子樹最大值or右子樹最小值)的爸爸
        if(ptr->Left) {
            left_max = ptr->Left;//往左一次
            while(left_max->Right) {//開始往右找最大
                tmp = left_max;
                left_max = left_max->Right;
            }
            if(tmp) {//tmp存在
                tmp->Right = left_max->Left;
                left_max->Left = ptr->Left;
            }
            left_max->Right = ptr->Right;

            return left_max;//新頭

        } else if (ptr->Right) {
            right_min = ptr->Right;//往右一次
            while(right_min->Left) {//開始往左找最小
                tmp = right_min;
                right_min = right_min->Left;
            }
            if(tmp) {//tmp存在
                tmp->Left = right_min->Right;
                right_min->Right = ptr->Right;
            }
            return right_min;//新頭

        }
    } else { //刪除節點不是頭
        Tree *tmp = NULL;
        if(ptr->Left) {//左子存在
            //找左子最大
            left_max = ptr->Left;
            while(left_max->Right) {
                tmp = left_max;
                left_max = left_max->Right;
            }
            //tmp存在
            if(tmp) {
                tmp->Right = left_max->Left;
                left_max->Left = ptr->Left;
            }
            left_max->Right = ptr->Right;
            if(pre->Left == ptr){
                pre->Left = left_max;
            }else{
                pre->Right = left_max;
            }
        } else if (ptr->Right) {//僅有右子
            //找右子最小
            right_min = ptr->Right;
            while(right_min->Left) {
                tmp = right_min;
                right_min = right_min->Left;
            }
            //tmp存在
            if(tmp) {
                tmp->Left = right_min->Right;
                right_min->Right = ptr->Right;
            }
            right_min->Left = ptr->Left;
            if(pre->Left == ptr){
                pre->Left = right_min;
            }else{
                pre->Right = right_min;
            }
        }else if (!ptr->Right && !ptr->Left) {//刪除節點為樹葉
            if(STRCMP(pre->selling,ptr->selling)>0) {
                pre->Left = NULL;
            } else {
                pre->Right = NULL;
            }

        }
        return products;
    }
}

Tree* buy(Tree *products,int counting[][5],FILE *fptr,FILE *read){
    printf("---buy---\n");
    char productName[MAXName]={0};
    fscanf(read,"%s",productName);

    int have_same = 0;
    Tree *ptr = search(productName,products,&have_same);
    if(have_same){
        printf("%s k%s %d\n",productName,ptr->info[1].seller,ptr->info[1].cost);
        fprintf(fptr,"%s %s %d\n",productName,ptr->info[1].seller,ptr->info[1].cost);

        if(ptr->amount>1){
            del_min(ptr->info,&(ptr->amount));
        }else{
            products = del_BST(products,productName);
        }
    }else{
        counting[1][2]++;
        printf("%s doesn’t exist!\n",productName);
        fprintf(fptr,"%s doesn’t exist!\n",productName);

    }
//    printf("%p",products);
//    ptr = products;
//    inorder(ptr);
    return products;
}

int main() {
    int counting[2][5]={0};
    FILE *SearchTable,*BuyTable,*SortTable,*LogTable,*Input;
    SearchTable = FOPEN("SearchTable.txt","w");
    BuyTable = FOPEN("BuyTable.txt","w");
    SortTable = FOPEN("SortTable.txt","w");

    char input[MAXName]={0};
    printf("Input file name:");
    scanf("%s",input);
    Input = FOPEN(input,"r");
    char choose[MAXName]= {0};
    Tree *products = NULL;
    while(fscanf(Input,"%s",choose)!=EOF) {
//        printf("%s\n",choose);

        if(!STRCMP(choose,"insert")) {
            counting[0][0]++;
            products = insert(products,Input);
        } else if(!STRCMP(choose,"search")) {
            counting[0][1]++;
            prod_search(products,counting,SearchTable,Input);
        } else if(!STRCMP(choose,"buy")) {
            counting[0][2]++;
            products = buy(products,counting,BuyTable,Input);
        } else if(!STRCMP(choose,"sort")) {
            printf("---inorder---\n");
            inorder(products,counting,1,SortTable);
            printf("------------------------------\n");
            fprintf(SortTable,"------------------------------\n");
        } else if(!STRCMP(choose,"report")) {
            LogTable = FOPEN("LogTable.txt","w");
            printf("insert %d\n",counting[0][0]);
            fprintf(LogTable,"insert %d\n",counting[0][0]);
            printf("search %d %d\n",counting[0][1],counting[1][1]);
            fprintf(LogTable,"search %d %d\n",counting[0][1],counting[1][1]);
            printf("buy %d %d\n",counting[0][2],counting[1][2]);
            fprintf(LogTable,"buy %d %d\n",counting[0][2],counting[1][2]);
            counting[0][3] = 0;
            counting[0][4] = 0;
            inorder(products,counting,0,NULL);
            printf("node_num %d\n",counting[0][3]);
            fprintf(LogTable,"node_num %d\n",counting[0][3]);
            printf("height %d\n",counting[0][4]);
            fprintf(LogTable,"height %d\n",counting[0][4]);


        } else {
            printf("Choose Error\n");
        }
    }
    return 0;
}
/*
Insert CD Mary 280 //新增 CD 這個商品以及賣家 Mary 賣此商品 280 元
insert TV John 20000 //新增 TV 這項商品以及賣家 John 賣此商品 20000元
buy Book //購買 Book 這項商品
insert Book Jacky08 220 //新增 Book 這項商品及賣家 Jacky08 賣此商品 220元
search CD //搜尋 CD 的所有賣家資料
insert Book Jacky01 200 //新增 Book 這項商品以賣家 Jacky01 賣此商品 200元
insert Book GiGi 180 //新增 Book 這項商品以及賣家 GiGi 賣此商品 180元
search Book //搜尋 Book 的所有賣家資料
buy Book //購買 Book 這項商品
buy Book //購買 Book 這項商品
search Notebook //搜尋 Notebook 的所有賣家資料
sort //列出目前拍賣的所有商品名稱
buy CD //購買 CD 這項商品
sort //列出目前拍賣的所有商品
report //產生 report tables
Insert CD Mary 280 //新增 CD 這個商品以及賣家 Mary 賣此商品 280 元
insert TV John 20000 //新增 TV 這項商品以及賣家 John 賣此商品 20000元
buy Book //購買 Book 這項商品
insert Book Jacky08 220 //新增 Book 這項商品及賣家 Jacky08 賣此商品 220元
search CD //搜尋 CD 的所有賣家資料
insert Book Jacky01 200 //新增 Book 這項商品以賣家 Jacky01 賣此商品 200元
insert Book GiGi 180 //新增 Book 這項商品以及賣家 GiGi 賣此商品 180元
search Book //搜尋 Book 的所有賣家資料
buy Book //購買 Book 這項商品
buy Book //購買 Book 這項商品
search Notebook //搜尋 Notebook 的所有賣家資料
sort //列出目前拍賣的所有商品名稱
buy CD //購買 CD 這項商品
sort //列出目前拍賣的所有商品
report //產生 report tables
Insert CD Mary 280 //新增 CD 這個商品以及賣家 Mary 賣此商品 280 元
insert TV John 20000 //新增 TV 這項商品以及賣家 John 賣此商品 20000元
buy Book //購買 Book 這項商品
insert Book Jacky08 220 //新增 Book 這項商品及賣家 Jacky08 賣此商品 220元
search CD //搜尋 CD 的所有賣家資料
insert Book Jacky01 200 //新增 Book 這項商品以賣家 Jacky01 賣此商品 200元
insert Book GiGi 180 //新增 Book 這項商品以及賣家 GiGi 賣此商品 180元
search Book //搜尋 Book 的所有賣家資料
buy Book //購買 Book 這項商品
buy Book //購買 Book 這項商品
search Notebook //搜尋 Notebook 的所有賣家資料
sort //列出目前拍賣的所有商品名稱
buy CD //購買 CD 這項商品
sort //列出目前拍賣的所有商品
report //產生 report tables
*/
