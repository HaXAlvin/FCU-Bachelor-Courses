#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#define MAXSeller 10
#define MAXName 20

typedef struct Data {
    char seller[MAXName];//��a
    int cost;//�ӫ~����
} Data;

typedef struct Tree {
    char selling[MAXName];//�ӫ~�W
    int amount;//�ӫ~��
    Data info[MAXSeller];//min heap
    struct Tree *Left,*Right;
} Tree;

Tree* MALLOC() {//tree��l��
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

char* TOLOWER(char *tmp){//�^�Ǥp�g���r��
    int i;
    char *output = malloc(sizeof(tmp));
    strcpy(output,tmp);
    for(i=0;i<strlen(output);i++){
        output[i] = tolower(output[i]);
    }
    return output;
}

int STRCMP(char *a,char *b){//���ݤj�p�g��strcmp
    return strcmp(TOLOWER(a),TOLOWER(b));
}

void sort_heap(Data heap[],int cost,char *seller,int *amount) {//�[�J�s��seller �P�ɱƧ�heap
    int i = ++(*amount);//�P�ӫ~�ƶq++
    while(i!=1&&cost<heap[i/2].cost) {//�Ncost�������U��
        heap[i].cost = heap[i/2].cost;
        strcpy(heap[i].seller,heap[i/2].seller);
        i=i/2;
    }
    heap[i].cost=cost;
    strcpy(heap[i].seller,seller);
}

Tree* search(char productName[],Tree *products,int *have_same) { //�^�� ���p �k�j
    printf("---search---\n");
    Tree *ptr = products,*pre=NULL;//ptr�s�ؼЦ�} pre�s�ؼЪ�����
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
    return pre;//�p�G�䤣��N�^�������m������
}

void inorder(Tree *ptr,int counting[][5],int way,FILE *fptr) {//way=0�ȹB�� way=1�B��+��X
    static int height=1;//�p��̤j����
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

Tree* insert(Tree *products,FILE *fptr) {//�s�W���
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
        products->info[1].cost = cost;//heap�q1�}�l
        strcpy(products->info[1].seller,seller);

    } else { //other node
        have_same=0;//���L�P�ӫ~
        ptr = search(productName,products,&have_same); //�I�ssearch��P�ӫ~�����s�Wnode����m
        if(have_same) { //���P�ӫ~
            sort_heap(ptr->info,cost,seller,&(ptr->amount));//�I�ssort_heap�s�W��ƶiheap
            printf("ADD Same:%s\n",ptr->selling);
        } else { //�s�ӫ~
            Tree *tmp = MALLOC();
            tmp->amount = 1;
            strcpy(tmp->selling,productName);
            tmp->info[1].cost = cost;
            strcpy(tmp->info[1].seller,seller);
            if(STRCMP(productName,ptr->selling)<0) {//�P�_����b���������l�٬O�k�l
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

void prod_search(Tree *products,int counting[][5],FILE *fptr,FILE *read){//product search�\��
    printf("---prod---\n");
    char productName[MAXName]={0};
    int have_same=0;//�O�_�����ӫ~
    fscanf(read,"%s",productName);
    Tree *ptr = search(productName,products,&have_same);
    if(have_same){//�����ӫ~
        printf("%s\n",ptr->selling);
        fprintf(fptr,"%s\n",ptr->selling);
        for(int i=1;i<=ptr->amount;i++){
            printf("%s %d\n",ptr->info[i].seller,ptr->info[i].cost);
            fprintf(fptr,"%s %d\n",ptr->info[i].seller,ptr->info[i].cost);
        }
        printf("------------------------------\n");
        fprintf(fptr,"------------------------------\n");
    }else{//�L���ӫ~
        counting[1][1]++;
        printf("%s doesn��t exist!\n",productName);
        fprintf(fptr,"%s doesn��t exist!\n",productName);
    }
}

void del_min(Data info[],int *amount){//����cost�̤p��heap
    printf("---del_min---\n");
    int min = info[1].cost;//�̤p��
    Data last = info[(*amount)--];//�̫�@�Ө�l
    int parent=1,child=2;
    while(child <= *amount)
    {
        if(child<=*amount && info[child].cost > info[child+1].cost)//��p�����l�Υk�l
            child++;
        if(min > info[child].cost)//���s���̤p
            break;
        //����U�@�h
        info[parent] = info[child];
        parent = child;
        child*=2;
    }
    info[parent] = last;//�N�̫�@�ө��W��
    while(parent!=1)//�T�w���ǵL�~
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

Tree* del_BST(Tree *products,char *need) {//�R��tree�`�I
    printf("---del---\n");
    Tree *pre = NULL,*left_max=NULL,*right_min=NULL,*ptr = products;
    if(!products) {
        printf("tree is empty\n");
        return (Tree*)NULL;
    }
    while(ptr) {//ptr=�ؼ�node�Apre=�ؼ�node������
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

    //pre=NULL�N��R����node�O�Y
    if(!pre) {
        Tree *tmp = NULL;//tmp=�ؼЪ�(���l��̤j��or�k�l��̤p��)������
        if(ptr->Left) {
            left_max = ptr->Left;//�����@��
            while(left_max->Right) {//�}�l���k��̤j
                tmp = left_max;
                left_max = left_max->Right;
            }
            if(tmp) {//tmp�s�b
                tmp->Right = left_max->Left;
                left_max->Left = ptr->Left;
            }
            left_max->Right = ptr->Right;

            return left_max;//�s�Y

        } else if (ptr->Right) {
            right_min = ptr->Right;//���k�@��
            while(right_min->Left) {//�}�l������̤p
                tmp = right_min;
                right_min = right_min->Left;
            }
            if(tmp) {//tmp�s�b
                tmp->Left = right_min->Right;
                right_min->Right = ptr->Right;
            }
            return right_min;//�s�Y

        }
    } else { //�R���`�I���O�Y
        Tree *tmp = NULL;
        if(ptr->Left) {//���l�s�b
            //�䥪�l�̤j
            left_max = ptr->Left;
            while(left_max->Right) {
                tmp = left_max;
                left_max = left_max->Right;
            }
            //tmp�s�b
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
        } else if (ptr->Right) {//�Ȧ��k�l
            //��k�l�̤p
            right_min = ptr->Right;
            while(right_min->Left) {
                tmp = right_min;
                right_min = right_min->Left;
            }
            //tmp�s�b
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
        }else if (!ptr->Right && !ptr->Left) {//�R���`�I����
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
        printf("%s doesn��t exist!\n",productName);
        fprintf(fptr,"%s doesn��t exist!\n",productName);

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
Insert CD Mary 280 //�s�W CD �o�Ӱӫ~�H�ν�a Mary �榹�ӫ~ 280 ��
insert TV John 20000 //�s�W TV �o���ӫ~�H�ν�a John �榹�ӫ~ 20000��
buy Book //�ʶR Book �o���ӫ~
insert Book Jacky08 220 //�s�W Book �o���ӫ~�ν�a Jacky08 �榹�ӫ~ 220��
search CD //�j�M CD ���Ҧ���a���
insert Book Jacky01 200 //�s�W Book �o���ӫ~�H��a Jacky01 �榹�ӫ~ 200��
insert Book GiGi 180 //�s�W Book �o���ӫ~�H�ν�a GiGi �榹�ӫ~ 180��
search Book //�j�M Book ���Ҧ���a���
buy Book //�ʶR Book �o���ӫ~
buy Book //�ʶR Book �o���ӫ~
search Notebook //�j�M Notebook ���Ҧ���a���
sort //�C�X�ثe��檺�Ҧ��ӫ~�W��
buy CD //�ʶR CD �o���ӫ~
sort //�C�X�ثe��檺�Ҧ��ӫ~
report //���� report tables
Insert CD Mary 280 //�s�W CD �o�Ӱӫ~�H�ν�a Mary �榹�ӫ~ 280 ��
insert TV John 20000 //�s�W TV �o���ӫ~�H�ν�a John �榹�ӫ~ 20000��
buy Book //�ʶR Book �o���ӫ~
insert Book Jacky08 220 //�s�W Book �o���ӫ~�ν�a Jacky08 �榹�ӫ~ 220��
search CD //�j�M CD ���Ҧ���a���
insert Book Jacky01 200 //�s�W Book �o���ӫ~�H��a Jacky01 �榹�ӫ~ 200��
insert Book GiGi 180 //�s�W Book �o���ӫ~�H�ν�a GiGi �榹�ӫ~ 180��
search Book //�j�M Book ���Ҧ���a���
buy Book //�ʶR Book �o���ӫ~
buy Book //�ʶR Book �o���ӫ~
search Notebook //�j�M Notebook ���Ҧ���a���
sort //�C�X�ثe��檺�Ҧ��ӫ~�W��
buy CD //�ʶR CD �o���ӫ~
sort //�C�X�ثe��檺�Ҧ��ӫ~
report //���� report tables
Insert CD Mary 280 //�s�W CD �o�Ӱӫ~�H�ν�a Mary �榹�ӫ~ 280 ��
insert TV John 20000 //�s�W TV �o���ӫ~�H�ν�a John �榹�ӫ~ 20000��
buy Book //�ʶR Book �o���ӫ~
insert Book Jacky08 220 //�s�W Book �o���ӫ~�ν�a Jacky08 �榹�ӫ~ 220��
search CD //�j�M CD ���Ҧ���a���
insert Book Jacky01 200 //�s�W Book �o���ӫ~�H��a Jacky01 �榹�ӫ~ 200��
insert Book GiGi 180 //�s�W Book �o���ӫ~�H�ν�a GiGi �榹�ӫ~ 180��
search Book //�j�M Book ���Ҧ���a���
buy Book //�ʶR Book �o���ӫ~
buy Book //�ʶR Book �o���ӫ~
search Notebook //�j�M Notebook ���Ҧ���a���
sort //�C�X�ثe��檺�Ҧ��ӫ~�W��
buy CD //�ʶR CD �o���ӫ~
sort //�C�X�ثe��檺�Ҧ��ӫ~
report //���� report tables
*/
