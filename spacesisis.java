package jp.jaxa.iss.kibo.rpc.sampleapk;

import org.opencv.core.Mat;
import org.opencv.objdetect.QRCodeDetector;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
    @Override
    protected void runPlan1(){
        api.judgeSendStart();

        moveToWrapper(11.45, -5.7, 4.5, 0, 0, 0, 1); //p1-1
        Mat snapshot = api.getMatNavCam();
        String valueX = Convert(snapshot);
        api.judgeSendDiscoveredQR(0, valueX);
       
        moveToWrapper(11, -6, 5.55, 0, -0.7071068, 0, 0.7071068); //p1-2
        Mat snapshot = api.getMatNavCam();
        String valueY = Convert(snapshot);
        api.judgeSendDiscoveredQR(1, valueY);
        
        moveToWrapper(11, -5.5, 4.33, 0, -0.7071068, 0, 0.7071068);//p1-3
        Mat snapshot = api.getMatNavCam();
        String valueZ = Convert(snapshot);
        api.judgeSendDiscoveredQR(2, valueZ);
        
        moveToWrapper(10.55, -5.5, 4.9, 0, 0, 1, 0);
        moveToWrapper(10.55, -6.8, 4.9, 0, 0, 1, 0);
        moveToWrapper(11.2, -6.8, 4.9, 0, 0, 1, 0);
        moveToWrapper(11.2, -7.5, 4.9, 0, 0, 1, 0);
        moveToWrapper(10.45, -7.5, 4.7, 0, 0, 1, 0);//p2-1
        Mat snapshot = api.getMatNavCam();
        String valueqX = Convert(snapshot);
        api.judgeSendDiscoveredQR(3, valueqX);
        
        moveToWrapper(11, -7.7, 5.55, 0, -0.7071068, 0, 0.7071068);//p2-3
        Mat snapshot = api.getMatNavCam();
        String valueqZ = Convert(snapshot);
        api.judgeSendDiscoveredQR(4, valueqZ);
        
        moveToWrapper(11.45, -8, 5, 0, 0, 0, 1);//p2-2
        Mat snapshot = api.getMatNavCam();
        String valueqY = Convert(snapshot);
        api.judgeSendDiscoveredQR(5, valueqY);
        
        moveToWrapper(11.45, -8, 4.65, 0, 0, 0, 1);
        moveToWrapper(11.1, -8, 4.65, 0, 0, 0, 1);
        moveToWrapper(11.1, -9, 4.65, 0, 0, 0, 1);//near to target Point
        
        moveToWrapper(valueX, valueY, valueZ, valueqX,valueqY,valueqZ, 1);
        
        api.laserControl(true);
        moveToWrapper(11.1, -6, 5.55, 0, -0.7071068, 0, 0.7071068);

        api.judgeSendFinishSimulation();
    }

    @Override
    protected void runPlan2(){
        // write here your plan 2
    }

    @Override
    protected void runPlan3(){
        // write here your plan 3
    }

    // You can add your method
    private void moveToWrapper(double pos_x, double pos_y, double pos_z,
                               double qua_x, double qua_y, double qua_z,
                               double qua_w){

        final int LOOP_MAX = 3;
        final Point point = new Point(pos_x, pos_y, pos_z);
        final Quaternion quaternion = new Quaternion((float)qua_x, (float)qua_y,
                (float)qua_z, (float)qua_w);

        Result result = api.moveTo(point, quaternion, true);

        int loopCounter = 0;
        while(!result.hasSucceeded() || loopCounter < LOOP_MAX){
            result = api.moveTo(point, quaternion, true);
            ++loopCounter;
        }

    }

    private static String Convert(Mat imgs){
        QRCodeDetector detectAndDecode = new QRCodeDetector();
        String value = detectAndDecode.detectAndDecode(imgs);
        return value;
    }

}
