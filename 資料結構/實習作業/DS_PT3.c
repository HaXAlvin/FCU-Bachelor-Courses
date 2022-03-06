#include <stdio.h>
#include <stdlib.h>
#define MAXNode 100

typedef struct Node{//節點
    int value;
    struct Node *left,*right;
}Node;

Node* NodeMalloc(){//malloc + 初始化
    Node *tmp = (Node*)malloc(sizeof(Node));
    if(tmp == NULL){
        printf("Malloc Error\n");
        exit(EXIT_FAILURE);
    }
    tmp->left = NULL;
    tmp->right = NULL;
    tmp->value = -1;
    return tmp;
}

//用InOrder和PostOrder建立Tree
void ORDER(int postOrder[],int inOrder[],int *charPlace,Node *now,int from,int to){//from、to是節點的value在InOrder中的範圍
    Node *nowLeft = NodeMalloc();
    Node *nowRight = NodeMalloc();
    (*charPlace)-=1;//當前在postOrder中的目標
    if(to > from){//範圍非0即為當前分支還沒結束
        for(int i=from;i<=to;i++){//尋找範圍內與現在目標相同字元的Index
            if(inOrder[i]==postOrder[*charPlace]){
                now->value = inOrder[i];

                if(i == to){//僅有左子
                    free(nowRight);
                    now->left = nowLeft;//link
                    ORDER(postOrder,inOrder,charPlace,nowLeft,from,i-1);

                }else if(i == from){//僅有右子
                    free(nowLeft);
                    now->right = nowRight;//link
                    ORDER(postOrder,inOrder,charPlace,nowRight,i+1,to);

                }else if(to>i && i>from){//有左右子
                    now->right = nowRight;//link
                    now->left = nowLeft;//link
                    ORDER(postOrder,inOrder,charPlace,nowRight,i+1,to);//先找右分支
                    ORDER(postOrder,inOrder,charPlace,nowLeft,from,i-1);//再找左分支

                }else{
                    printf("Location Error\n");
                    exit(EXIT_FAILURE);
                }
                break;
            }
        }
    }else if (to == from){//範圍為0代表現在的節點是葉
        now->value = inOrder[from];
    }else{
        printf("Index Error\n");
        exit(EXIT_FAILURE);
    }
}
void DEAL(Node *now,int *max,int *min){
    static int count=0;
    count+=now->value;
    if(now->left){
        DEAL(now->left,max,min);
    }
    if(now->right){
        DEAL(now->right,max,min);
    }
    if(now->right==NULL && now->left==NULL){
        if(count > *max){
            *max = count;
        }
        if(count < *min){
            *min = count;
        }
    }
    count-=now->value;
}
int main()
{
    char control = 0;
    int inOrder[MAXNode]={0},postOrder[MAXNode]={0},len=0,max=0,min=0;
    while(scanf("%d%c",&inOrder[len],&control)){
        min+=inOrder[len];
        len++;
        if(control=='\n'){
            len=0;
            control = 0;
            break;
        }
    }
    while(scanf("%d%c",&postOrder[len],&control)){
        len++;
        if(control=='\n'){
            break;
        }
    }
    Node *tree = NodeMalloc();
    ORDER(postOrder,inOrder,&len,tree,0,len-1);//第一個節點範圍是全部
    DEAL(tree,&max,&min);
    printf("Delayed time (longest path) = %d\nDelayed time (shortest path) = %d\n",max,min);
    return 0;
}

/*
13 18 30 51 5 22 7 2 42
13 18 51 7 22 5 42 2 30
*/
