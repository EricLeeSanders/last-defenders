package simulate;

import com.badlogic.gdx.math.Vector2;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimulationMathUtil {
    public static float intersectSegmentCircleDisplace(Vector2 start, Vector2 end, Vector2 point, float radius, Vector2 displacement) {

        Vector2 tmp = new Vector2();
        Vector2 tmp2 = new Vector2();

        float u = (point.x - start.x) * (end.x - start.x) + (point.y - start.y) * (end.y - start.y);
        float d = start.dst(end);
        u /= d * d;
        if (u < 0)
            tmp2.set(start);
        else if (u > 1)
            tmp2.set(end);
        else {
            tmp.set(end.x, end.y).sub(start.x, start.y);
            tmp2.set(start.x, start.y).add(tmp.scl(u));
        }
        d = tmp2.dst(point.x, point.y);
        if (d < radius) {
            displacement.set(tmp2).sub(point).nor();
            return radius - d;
        } else
            return Float.POSITIVE_INFINITY;
    }

    public static List<Vector2> getCircleLineIntersectionPoint(Vector2 pointA,
        Vector2 pointB, Vector2 center, float radius) {
        float baX = pointB.x - pointA.x;
        float baY = pointB.y - pointA.y;
        float caX = center.x - pointA.x;
        float caY = center.y - pointA.y;

        float a = baX * baX + baY * baY;
        float bBy2 = baX * caX + baY * caY;
        float c = caX * caX + caY * caY - radius * radius;

        float pBy2 = bBy2 / a;
        float q = c / a;

        double disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return Collections.emptyList();
        }
        // if disc == 0 ... dealt with later
        float tmpSqrt = (float)Math.sqrt(disc);
        float abScalingFactor1 = -pBy2 + tmpSqrt;
        float abScalingFactor2 = -pBy2 - tmpSqrt;

        Vector2 p1 = new Vector2(pointA.x - baX * abScalingFactor1, pointA.y
            - baY * abScalingFactor1);
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            return Collections.singletonList(p1);
        }
        Vector2 p2 = new Vector2(pointA.x - baX * abScalingFactor2, pointA.y
            - baY * abScalingFactor2);
        return Arrays.asList(p1, p2);
    }

    public static boolean inLine(Vector2 A, Vector2 B, Vector2 C) {
        if(C.x > A.x && C.x > B.x){
            return false;
        }
        if(C.x < A.x && C.x < B.x){
            return false;
        }

        if(C.y > A.y && C.y > B.y){
            return false;
        }
        if(C.y < A.y && C.y < B.y){
            return false;
        }

        return true;
    }
}
