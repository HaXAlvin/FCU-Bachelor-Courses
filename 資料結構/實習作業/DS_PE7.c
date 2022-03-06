#include <stdio.h>
#include <stdlib.h>
#define MAX 20
typedef struct link{
    int way;
    int many;
    struct link* danced[MAX];
}link;

void order(link* NowDancer){
    int i,flag=0;
    for(i=0;i<NowDancer->many;i++){
        if(NowDancer->danced[i]->way > NowDancer->way+1){
            flag=1;
            NowDancer->danced[i]->way = NowDancer->way+1;
        }
    }
//    for(i=1;i<=4;i++){
//        printf("%d\n",dancer[i].way);
//    }
    for(i=0;i<NowDancer->many && flag==1;i++){
        order(NowDancer->danced[i]);
    }
}

int main(){
    int i,j,times,peoples,inputs,person_1,person_2;

    scanf("%d",&times);
    for(;times>0;times--){

        scanf("%d %d",&peoples,&inputs);
        link dancer[peoples];
        for(i=0;i<peoples;i++){//初始化
            dancer[i].way = MAX+1;
            dancer[i].many = 0;
            for(j=0;j<MAX;j++){
                dancer[i].danced[j] = NULL;
            }
        }
        for(;inputs>0;inputs--){

            scanf("%d %d",&person_1,&person_2);
            dancer[person_1].danced[dancer[person_1].many] = &dancer[person_2];
            dancer[person_1].many++;
            dancer[person_2].danced[dancer[person_2].many] = &dancer[person_1];
            dancer[person_2].many++;
        }
        dancer[0].way = 0;
        order(&dancer[0]);
        for(i=1;i<peoples;i++){
            printf("%d\n",dancer[i].way);
        }
    }
    return 0;
}
/*
2

5 6
0 1
0 2
3 2
2 4
4 3
1 2

5 5
1 2
0 1
2 3
3 4
0 4
*/
