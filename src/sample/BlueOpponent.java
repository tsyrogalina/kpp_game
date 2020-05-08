package sample;

import java.util.Vector;

public class BlueOpponent implements Opponent {
    private Rect srcR;              //место расположеняи привидения на спрайте
    private Rect destR;             //место расположения привидения на карте
    private double scale;
    private final int startX = 26;
    private final int startY = 29;

    private DIR direction = DIR.LEFT;
    private int frame = 0;
    private int pos = 0;
    private double speed;
    private int height;
    private int weight;
    Map map;
    private boolean flagEatBonus = false;

    private Rect goalTile ;
    private OpponentMode opponentMode = OpponentMode.SCATTER;
    public Rect getDestR(){return destR;}

    public DIR getDirection() {
        return direction;
    }

    public Rect getSrcR() {
        return srcR;
    }

    public int getFrame() {
        return frame;
    }

    public int getPos() {
        return pos;
    }

    public Rect getGoalTile() {
        return goalTile;
    }

    public OpponentMode getOpponentMode() {
        return opponentMode;
    }
    public boolean getFlagEatBonus()
    {
        return flagEatBonus;
    }

    public void setDestR(Rect destR) {
        this.destR = destR;
    }

    public void setDirection(DIR direction) {
        this.direction = direction;
    }

    public void setFlagEatBonus(boolean flagEatBonus) {
        this.flagEatBonus = flagEatBonus;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public void setGoalTile(Rect goalTile) {
        this.goalTile = goalTile;
    }

    public void setOpponentMode(OpponentMode opponentMode) {
        this.opponentMode = opponentMode;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setSrcR(Rect srcR) {
        this.srcR = srcR;
    }
    @Override
    public void restart(){
        opponentMode = OpponentMode.SCATTER;
        goalTile = new Rect((weight-1)*scale+0.5,height*scale+0.5,scale-1,scale-1);

        srcR = new Rect(82.5, 122.5, 15, 15);
        frame = 0;
        direction = DIR.LEFT;
        destR = new Rect(startX*scale+0.5, startY*scale+0.5, scale-1, scale-1);

    }

    @Override
    public void init(Map map, double scale, int height, int wight) {
        this.weight = wight;
        this.height = height;
        this.scale = scale;
        this.map = map;
        goalTile = new Rect((weight-1)*scale+0.5,height*scale+0.5,scale-1,scale-1);

        srcR = new Rect(82.5, 122.5, 15, 15);
        destR = new Rect(startX*scale+0.5, startY*scale+0.5, scale-1, scale-1);

        speed = scale/10;


    }

    @Override
    public boolean update(Rect pacmanR, DIR pacmanDIR, int score) {
        if(destR.x==0.5)
            direction = DIR.RIGHT;
        else if(destR.x==(weight -1)*scale+0.5)
            direction = DIR.LEFT;
        setMode(pacmanR,pacmanDIR,score,map.getSuperBonusArray());
        if(Collision.AABB(destR,pacmanR))
        {
            if(opponentMode == OpponentMode.SCARE) {
                destR.x = 12*scale+0.5;
                destR.y = 14*scale+0.5;
                speed =scale/10;
                opponentMode= OpponentMode.CHASE;

                frame =0;
                return true;
            }
            else{
                return false;//game over
            }



        }

        if (opponentMode == OpponentMode.SCARE) {
            if(frame%20==0)
                typicalBehavior();
        }
        else {
            if(frame%10==0 )
                typicalBehavior();
        }

        switch (direction){

            case DOWN:       destR.y += speed;      break;
            case RIGHT:      destR.x += speed;      break;
            case LEFT:       destR.x -= speed;      break;
            case UP:         destR.y -= speed;      break;
        }

        animation();
        return true;
    }

    @Override
    public void animation() {
        frame++;
        if(frame%delay==0)
        {
            pos++;

        }
        if(pos%2==0)
        {
            if(opponentMode==OpponentMode.SCARE)
            {

                srcR.y = 162.5;
                srcR.x = 2.5;

            }
            else if(direction==DIR.UP)
            {
                srcR.x =2.5;
                srcR.y =122.5;
            }
            else if(direction==DIR.DOWN)
            {
                srcR.x= 42.5;
                srcR.y =122.5;
            }
            else if(direction==DIR.LEFT)
            {
                srcR.x =82.5;
                srcR.y =122.5;
            }
            else if(direction==DIR.RIGHT)
            {
                srcR.x= 122.5;
                srcR.y =122.5;
            }
        }
        else
        {
            if(opponentMode==OpponentMode.SCARE)
            {

                if(frame<=300)
                {
                    srcR.y = 162.5;
                    srcR.x = 22.5;
                }
                else
                {
                    srcR.y = 162.5;
                    srcR.x = 62.5;
                }

            }

            else if(direction==DIR.UP)
            {
                srcR.x =22.5;
                srcR.y =122.5;

            }
            else if(direction==DIR.DOWN)
            {
                srcR.x= 62.5;
                srcR.y =122.5;
            }
            else if(direction==DIR.LEFT)
            {
                srcR.x =102.5;
                srcR.y =122.5;
            }
            else if(direction==DIR.RIGHT)
            {
                srcR.x= 142.5;
                srcR.y =122.5;
            }
        }

    }

    @Override
    public void typicalBehavior() {
        double vectors[] = {0, 0, 0, 0}; //left,right,up,down
        Rect rect = destR.copy();
        if(direction == DIR.RIGHT)
        {
            vectors[0] = -2;
            rect.x+=speed;
            for (Rect r : map.getBorderArray()) {
                if (Collision.AABB(rect, r)) {
                    vectors[1]=-1;                   //если забор то сюда идти нельзя

                    break;                               //проверяем все заборики
                }
            }
            rect.x-=speed;

            rect.y-=speed;
            for (Rect r : map.getBorderArray()) {
                if (Collision.AABB(rect, r)) {
                    vectors[2]=-1;                   //если забор то сюда идти нельзя

                    break;                               //проверяем все заборики
                }
            }
            rect.y+=speed;

            rect.y+=speed;
            for (Rect r : map.getBorderArray()) {
                if (Collision.AABB(rect, r)) {
                    vectors[3]=-1;                   //если забор то сюда идти нельзя

                    break;                               //проверяем все заборики
                }
            }
            rect.y-=speed;

        }
        else if(direction == DIR.LEFT)
        {
            vectors[1] = -2;
            rect.x-=speed;
            for (Rect r : map.getBorderArray()) {
                if (Collision.AABB(rect, r)) {
                    vectors[0]=-1;                   //если забор то сюда идти нельзя

                    break;                               //проверяем все заборики
                }
            }
            rect.x+=speed;

            rect.y-=speed;
            for (Rect r : map.getBorderArray()) {
                if (Collision.AABB(rect, r)) {
                    vectors[2]=-1;                   //если забор то сюда идти нельзя

                    break;                               //проверяем все заборики
                }
            }
            rect.y+=speed;

            rect.y+=speed;
            for (Rect r : map.getBorderArray()) {
                if (Collision.AABB(rect, r)) {
                    vectors[3]=-1;                   //если забор то сюда идти нельзя

                    break;                               //проверяем все заборики
                }
            }
            rect.y-=speed;


        }
        else if(direction ==DIR.DOWN) {
            vectors[2] = -2;
            rect.x-=speed;
            for (Rect r : map.getBorderArray()) {
                if (Collision.AABB(rect, r)) {
                    vectors[0]=-1;                   //если забор то сюда идти нельзя

                    break;                               //проверяем все заборики
                }
            }
            rect.x+=speed;

            rect.x+=speed;
            for (Rect r : map.getBorderArray()) {
                if (Collision.AABB(rect, r)) {
                    vectors[1]=-1;                   //если забор то сюда идти нельзя

                    break;                               //проверяем все заборики
                }
            }
            rect.x-=speed;

            rect.y+=speed;
            for (Rect r : map.getBorderArray()) {
                if (Collision.AABB(rect, r)) {
                    vectors[3]=-1;                   //если забор то сюда идти нельзя

                    break;                               //проверяем все заборики
                }
            }
            rect.y-=speed;


        }
        else if(direction ==DIR.UP) {
            vectors[3] = -2;
            rect.x-=speed;
            for (Rect r : map.getBorderArray()) {
                if (Collision.AABB(rect, r)) {
                    vectors[0]=-1;                   //если забор то сюда идти нельзя

                    break;                               //проверяем все заборики
                }
            }
            rect.x+=speed;

            rect.x+=speed;
            for (Rect r : map.getBorderArray()) {
                if (Collision.AABB(rect, r)) {
                    vectors[1]=-1;                   //если забор то сюда идти нельзя

                    break;                               //проверяем все заборики
                }
            }
            rect.x-=speed;

            rect.y-=speed;
            for (Rect r : map.getBorderArray()) {
                if (Collision.AABB(rect, r)) {
                    vectors[2]=-1;                   //если забор то сюда идти нельзя

                    break;                               //проверяем все заборики
                }
            }
            rect.y+=speed;


        }

        Vector<DIR> availableDirections = new Vector<>();

        for(int i = 0; i < 4;i++)
        {
            if(vectors[i] ==0)
            {
                if(i==0)
                {
                    availableDirections.add(DIR.LEFT);
                    vectors[i] = Math.hypot(destR.x-scale-goalTile.x,destR.y-goalTile.y);
                }
                else if(i==1)
                {
                    availableDirections.add(DIR.RIGHT);
                    vectors[i] = Math.hypot(destR.x+scale-goalTile.x,destR.y-goalTile.y);              //считаем растояние по прямой по теореме пифагор
                }
                else if(i==2)
                {
                    availableDirections.add(DIR.UP);
                    vectors[i] = Math.hypot(destR.x-goalTile.x,destR.y-scale-goalTile.y);
                }
                else if(i==3) {
                    availableDirections.add(DIR.DOWN);
                    vectors[i] = Math.hypot(destR.x-goalTile.x,destR.y+scale-goalTile.y);
                }
            }

        }
        int count = 0;            //счетчик напрвлений куда ехать нельзя
        int min= 0;
        for(int i=0; i < 4;i++)
        {
            if(vectors[i]!=-1&&vectors[i]!=-2)
            {
                min = i;               //ищем первый элемнт отличный от -1 и -2
                break;
            }
        }

        for(int i = 0; i<4;i++)
        {
            if(vectors[i]==-1||vectors[i]==-2)
            {
                if(vectors[i]==-1)
                    count++;
                continue;
            }                                        //ищем минимальное расстояние
            if(vectors[min]>vectors[i])
            {
                min = i;
            }


        }

        if(count==3) {
            int direct = 0;
            for(int i = 0; i < 4;i++)
            {
                if(vectors[i]==-2)
                {
                    direct = i;
                    break;
                }
            }
            switch (direct) {
                case 0:
                    direction = DIR.LEFT;              //если впереди тольео стенки то разворачиваемся и едем назад
                    break;
                case 1:
                    direction = DIR.RIGHT;
                    break;
                case 2:
                    direction = DIR.UP;
                    break;
                case 3:
                    direction = DIR.DOWN;
            }
        }
        else if(opponentMode==OpponentMode.SCARE||opponentMode==OpponentMode.CHASE)
        {
            int n = (int)Math.floor(Math.random()*availableDirections.size());
            direction = availableDirections.elementAt(n);

        }
        else if(min==0)
        {
            direction=DIR.LEFT;
        }
        else if(min==1)
        {
            direction=DIR.RIGHT;
        }
        else if(min==2)
        {
            direction=DIR.UP;
        }
        else if(min==3)
        {
            direction=DIR.DOWN;
        }



    }

    @Override
    public void setMode(Rect pacmanR, DIR pacmanDIR, int score,Vector<Rect> superBonus) {
        for( int i = 0; i<superBonus.size();i++)
        {
            if(Collision.AABB(pacmanR,superBonus.get(i)))
            {
                flagEatBonus = true;
                superBonus.remove(i);
                map.getSuperBonusArraySource().remove(i);

            }
        }
        if(frame==420&&opponentMode == OpponentMode.SCARE)
        {
            speed =scale/10;
            opponentMode= OpponentMode.CHASE;
            frame=0;
            return;
        }
        if((flagEatBonus ==true&&frame%10==0&&opponentMode!=OpponentMode.SCARE)||(flagEatBonus ==true&&frame%20==0&&opponentMode==OpponentMode.SCARE)) {
            flagEatBonus = false;
            opponentMode = OpponentMode.SCARE;
            speed =scale/20;
            frame =0;
        }
        else if(frame==420&&opponentMode == OpponentMode.SCATTER)
        {
            speed =scale/10;
            opponentMode= OpponentMode.CHASE;


            frame=0;
        }
        else if(frame ==1200&&opponentMode == OpponentMode.CHASE)
        {
            opponentMode  = OpponentMode.SCATTER;
            speed =scale/10;

            frame =0;
        }
    }

    @Override
    public Rect setSrcRect() {
        return srcR;
    }

    @Override
    public Rect setDestRect() {
        return destR;
    }
}
