package sample;

public class Collision {
    public static boolean AABB(final Rect recA, final Rect recB) {
        //если сверху
        return recA.x + recA.w >= recB.x &&          //если слева от объекта A
                recB.x + recB.w >= recA.x &&         //сли справа от обьекта А
                recA.y + recA.h >= recB.y &&         //если снизу от обЬекта А
                recB.y + recB.h >= recA.y;           //если две клетки пересекаются
    }
}
