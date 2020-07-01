package com.example.ros_nodes;


import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.common.collect.Lists;

import com.github.rosjava.android_remocons.common_tools.apps.RosAppActivity;

import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosImageView;
import org.ros.android.view.RosTextView;
import org.ros.android.view.visualization.VisualizationView;
import org.ros.android.view.visualization.layer.LaserScanLayer;
import org.ros.android.view.visualization.layer.Layer;
import org.ros.android.view.visualization.layer.OccupancyGridLayer;
import org.ros.android.view.visualization.layer.RobotLayer;
import org.ros.namespace.NameResolver;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import com.example.ros_nodes.Talker;

import sensor_msgs.CompressedImage;


public class MainActivity extends RosActivity {
    private static final String TAG = "ROS_NODE_MAIN";
    private static final int MAKE_MAP_DIALOG_ID = 0;

    private NodeMainExecutor nodeMainExecutor;
    private NodeConfiguration nodeConfiguration;

    private RosImageView< sensor_msgs.CompressedImage > cameraView;

    private VisualizationView mapView;

//    private ViewGroup mainLayout;
//    private ViewGroup sideLayout;
//
//    private ImageButton refreshButton;
//    private ImageButton saveBUtton;

    private Button backButton;

    private RosTextView<std_msgs.String> rosTextView;

    private OccupancyGridLayer mapLayer = null;
    private LaserScanLayer laserScanLayer = null;
    private RobotLayer robotLayer = null;
    private Talker talker;

    public MainActivity(){

        super("Ros-Node-Notfication-ticker","ROS-Nodes-Notification-Title");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {


//        String defaultRobotName = getString(R.string.default_robot);
//        String defaultAppName = getString(R.string.default_app);
//        setDefaultMasterName(defaultRobotName);
//        setDefaultAppName(defaultAppName);
//        setDashboardResource(R.id.top_bar);
//        setMainWindowResource(R.layout.activity_main);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//        cameraView = (RosImageView< sensor_msgs.CompressedImage >) findViewById(R.id.image);
//        cameraView.setMessageType(CompressedImage._TYPE);
//        cameraView.setMessageToBitmapCallable(new BitmapFromCompressedImage());

        mapView = (VisualizationView) findViewById(R.id.map_view);
        mapView.onCreate(Lists.<Layer>newArrayList());

        mapView.getCamera().jumpToFrame("map");

        rosTextView = (RosTextView<std_msgs.String>) findViewById(R.id.text);
        rosTextView.setTopicName("/ros/android/chatter");
        rosTextView.setMessageType(std_msgs.String._TYPE);
        rosTextView.setMessageToStringCallable(new MessageCallable<String, std_msgs.String>() {
            @Override
            public String call(std_msgs.String message) {
                return message.getData();
            }
        });




    }


    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
//        super.init(nodeMainExecutor);

//        this.nodeMainExecutor = nodeMainExecutor;
        nodeConfiguration = NodeConfiguration.newPublic(getRosHostname(),getMasterUri());

        NameResolver appNameResolver = nodeConfiguration.getParentResolver();

        String mapTopic = getString(R.string.map_topic);

        mapLayer = new OccupancyGridLayer(getString(R.string.map_topic));

        mapView.addLayer(mapLayer);
        mapView.init(nodeMainExecutor);
        nodeMainExecutor.execute(mapView, nodeConfiguration.setNodeName("android/map_view"));

//        nodeMainExecutor.execute(talker, nodeConfiguration);

//        String camTopic = getString(R.string.camera_topic);
//        NameResolver appNameResolver = nodeConfiguration.getParentResolver();

        nodeMainExecutor.execute(rosTextView,nodeConfiguration.setNodeName("android/textView"));
//        nodeMainExecutor.execute(listener,nodeConfiguration);

    }
}