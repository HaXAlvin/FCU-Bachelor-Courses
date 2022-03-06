#include <stdio.h>
#include <stdlib.h>
#define MAX 200



int main(){

    int times;
    scanf("%d",&times);
    while(0<times--){
        int i,j,k,planets,cases,from,to,way,maxCost=0;
        int cost[MAX][MAX]={0};
        int found[MAX][MAX]={0};//flag

        for(i=0;i<MAX;i++){//ªì©l¤Æ
            for(j=0;j<MAX;j++){
                cost[i][j] = MAX*MAX+1;
            }
        }
        scanf("%d %d",&planets,&cases);
        for(i=0;i<cases;i++){
            scanf("%d %d %d",&from,&to,&way);
            cost[from-1][to-1] = way;
            cost[to-1][from-1] =  way;
//            for(j=0;j<planets;j++){
//                for(k=0;k<planets;k++){
//                    printf("%5d ",cost[j][k]);
//                }
//                puts("abc");
//            }
        }
        for(i=0;i<planets;i++){
            for(j=0;j<planets;j++){
                for(k=0;k<planets && i!=k;k++){
                    if(cost[i][k]>cost[i][j]+cost[j][k]){
                        cost[i][k] = cost[i][j]+cost[j][k];
                        cost[k][i] = cost[i][k];
//                        printf("change%d\n",cost[i][k]);
                    }
                }
            }
        }
        for(i=0;i<planets;i++){
            for(j=0;j<planets;j++){
                printf("%5d ",cost[i][j]);
            }
            puts("");
        }
        for(i=0;i<planets;i++){
            for(j=0;j<planets && j!=i;j++){
                for(k=0;k<planets;k++){
                    if(cost[i][k]>=cost[j][k]){
                        found[i][j]++;
                    }
                }

            }
        }
        for(i=0;i<planets;i++){
            for(j=0;j<planets && j!=i;j++){
                for(k=0;k<planets;k++){
                    if(cost[j][k]>=cost[i][k]){
                        found[j][i]++;
                    }
                }

            }
        }

        for(i=0;i<planets;i++){
            for(j=0;j<planets;j++){
                printf("%5d ",found[i][j]);
            }
            puts("");
        }
        for(i=0;i<planets;i++){
            for(j=0;j<planets;j++){
                if(maxCost<found[i][j]){
                    maxCost = found[i][j];
                }
            }
        }
        printf("\n\n\naaaaaa%d\n\n\n",maxCost);
    }
    return 0;
}
/*
3
3 3
1 2 5
2 3 8
3 1 6
5 5
1 2 1
1 3 1
1 4 1
1 5 1
3 4 1
5 5
1 2 1
1 3 1
2 3 1
3 4 1
3 5 1
*/

