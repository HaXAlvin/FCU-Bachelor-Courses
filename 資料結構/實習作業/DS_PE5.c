#include <stdio.h>
#include <stdlib.h>
#define MAX 100

int insert(int tree[],int input,int *nodes)
{
    int i = ++(*nodes);
    while(i!=1 && input<tree[i/2])
    {
        tree[i] = tree[i/2];
        i/=2;
    }
    tree[i]=input;
//    printf("input %d\n",input);
//    for(int i=0; i<*nodes; i++)
//    {
//        printf("%d ",tree[i+1]);
//    }
//    printf("\n");

    return input;
}

int del_min(int tree[],int *nodes)
{
    int min = tree[1];//最小值
//    printf("min %d\n",min);
    int last = tree[(*nodes)--];//最後一個兒子
    int parent=1,child=2;
    while(child <= *nodes)
    {
        if(child<=*nodes && tree[child] > tree[child+1])//找小的左兒子或右兒子
            child++;
        if(min > tree[child])
            break;
        //移到下一層
        tree[parent] = tree[child];
        parent = child;
        child*=2;
    }
    tree[parent] = last;
    while(parent!=1)
    {
        if(tree[parent]<tree[parent/2])
        {
            int tmp = tree[parent];
            tree[parent] = tree[parent/2];
            tree[parent/2] = tmp;
        }
        parent/=2;
    }
//    for(int i=0; i<*nodes; i++)
//    {
//        printf("%d ",tree[i+1]);
//    }
//    puts("");
    return min;
}

int main()
{


    FILE *fp;
    fp = fopen("myInput.txt","r");
    int many;
    while(fscanf(fp,"%d",&many)!=EOF)
    {
        int i,input=0,nodes=0;
        int tree[MAX]= {0};
        int num=0;

        for(i=0; i<many; i++)
        {
            fscanf(fp,"%d",&input);
            insert(tree,input,&nodes);
        }
//    for(i=0;i<many;i++){
//        printf("%d ",tree[i+1]);
//    }
        while(nodes!=1)
        {
            int a = del_min(tree,&nodes);
            int b = del_min(tree,&nodes);
//        printf("%d ",a+b);
            num+=insert(tree,a+b,&nodes);
        }
        printf("%d\n",num);
    }
}
