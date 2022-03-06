#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct node{
    int from,to;
    struct node *left,*right;
}node;

void ORDER(char PreOrder[],int *charPlace,char inOrder[],int numbers,node *now){
    node *nowLeft,*nowRight;
    (*charPlace)+=1;
    printf("%d %d\n",now->from,now->to);
    if(now->to-now->from!=0){
        for(int i=now->from;i<=now->to;i++){

            if(inOrder[i]==PreOrder[*charPlace]){

				//printf("c\n");
                if(i==now->to){
                    nowLeft =(node*)malloc(sizeof(node));
                    nowLeft->from = now->from;
                    nowLeft->to = i-1;
                    now->left = nowLeft;
                    now->right =NULL;
//                    now->from = i;
//                    now->to = i;
                    ORDER(PreOrder,charPlace,inOrder,numbers,nowLeft);
                }else if(i==now->from){
                    nowRight =(node*)malloc(sizeof(node));
                    nowRight->from = i+1;
                    nowRight->to = now->to;
                    now->right = nowRight;
                    now->left = NULL;
//                    now->from = i;
//                    now->to = i;
                    ORDER(PreOrder,charPlace,inOrder,numbers,nowRight);
                }else if(i<now->to&&i>now->from){
                    nowLeft =(node*)malloc(sizeof(node));
                    nowRight =(node*)malloc(sizeof(node));

                    nowLeft->from = now->from;
                    nowLeft->to = i-1;
                    nowRight->from = i+1;
                    nowRight->to = now->to;
                    now->left = nowLeft;
                    now->right = nowRight;
                    ORDER(PreOrder,charPlace,inOrder,numbers,nowLeft);

                    ORDER(PreOrder,charPlace,inOrder,numbers,nowRight);

                }else{
                    printf("Location Error\n");
                    exit(EXIT_FAILURE);
                }

                now->from = i;
                now->to = i;
                break;
            }
        }
    }else{
        now->left=NULL;
        now->right=NULL;
    }
}

void postOrder(char inOrder[],node *now,FILE *ffff){
    //printf("c %d\n",now->from);
    if(now->left)
        postOrder(inOrder,now->left,ffff);

    if(now->right)
        postOrder(inOrder,now->right,ffff);
    if(now)
        fprintf(ffff,"%c",inOrder[now->from]);


}
/*
9
ABCDEFGHI
BCAEDGHFI
*/
int main()
{
    int numbers;
    char preOrder[100]={0};
    char inOrder[100]={0};
    FILE *fptr=fopen("Input.txt","r");
    FILE *ffff=fopen("Output.txt","w");
    fscanf(fptr,"%d",&numbers);
    fscanf(fptr,"%s",preOrder);
    fscanf(fptr,"%s",inOrder);
    //printf("%d\n",numbers);
    node tree;
    tree.from=0;
    tree.to = numbers-1;
    int charPlace = -1;
    ORDER(preOrder,&charPlace,inOrder,numbers,&tree);
    postOrder(inOrder,&tree,ffff);
    return 0;
}
