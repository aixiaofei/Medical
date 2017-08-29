package com.example.ai.dtest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ai.dtest.base.BaseActivity;
import com.example.ai.dtest.data.DoctorCustom;
import com.example.ai.dtest.data.OrderDetailInfo;
import com.example.ai.dtest.util.HttpUtils;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.ai.dtest.base.MyApplication.getContext;

public class OrderDetail extends BaseActivity {

    private TextView docName;

    private TextView docDepartment;

    private TextView docPosition;

    private TextView docHosipital;

    private TextView docSpecialty;

    private TextView docComment;

    private TextView doctorPay;

    private TextView hosipitalInfo;

    private TextView hosipitalPay;

    private TextView time;

    private TextView allPay;

    private static final String IMAGEURI= HttpUtils.SOURCEIP+ "/internetmedical/user/getdoctorpix/";

    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HttpUtils.SINGLEDOFA:
                    break;
                case HttpUtils.SINGLEDOSU:
                    Bundle bundle1= msg.getData();
                    DoctorCustom info= (DoctorCustom) bundle1.getSerializable("result");
                    getDocInfo(info);
                    break;
                case HttpUtils.ORDERDETAILFA:
                    break;
                case HttpUtils.ORDERDETAILSU:
                    Bundle bundle2= msg.getData();
                    OrderDetailInfo info1=(OrderDetailInfo) bundle2.getSerializable("result");
                    getOrderInfo(info1);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent=getIntent();
        final int docLoginId = intent.getIntExtra("loginId", -1);
        int orderId= intent.getIntExtra("orderId",-1);
        int state= intent.getIntExtra("state",-1);

        CircleImageView imageView = (CircleImageView) findViewById(R.id.doctor_photo);
        loadImage(imageView,docLoginId);

        ImageView backFig= (ImageView) findViewById(R.id.design_back_fig);
        TextView backText= (TextView) findViewById(R.id.design_back_text);
        backFig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        docName= (TextView) findViewById(R.id.docter_name);
        docDepartment= (TextView) findViewById(R.id.docter_keshi);
        docPosition= (TextView) findViewById(R.id.docter_position);
        docHosipital= (TextView) findViewById(R.id.docter_hosipital);
        docSpecialty= (TextView) findViewById(R.id.docter_specialty);
        docComment= (TextView) findViewById(R.id.docter_comment);
        doctorPay= (TextView) findViewById(R.id.doctor_pay);
        hosipitalInfo= (TextView) findViewById(R.id.hosipital_info);
        hosipitalPay= (TextView) findViewById(R.id.hosipital_pay);
        time = (TextView) findViewById(R.id.time);
        allPay= (TextView) findViewById(R.id.all_pay);

        CardView docInfo= (CardView) findViewById(R.id.list_1);
        docInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoctorHomePage.actionStart(OrderDetail.this,docLoginId,2);
            }
        });

        CardView view= (CardView) findViewById(R.id.info_list);
        TextView orderState= (TextView) findViewById(R.id.state);
        TextView sureInfo= (TextView) findViewById(R.id.sure_desc);

        HttpUtils.singleDocterUpdata(docLoginId,handler);
        if(state<3){
            orderState.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
        }else if(state==3){
            sureInfo.setVisibility(View.VISIBLE);
            HttpUtils.orderDeatil(orderId,handler);
        }else {
            HttpUtils.orderDeatil(orderId,handler);
        }
    }

    private void getDocInfo(DoctorCustom info){
        docName.setText(info.getDocname());
        docDepartment.setText(info.getDocdept());
        docPosition.setText(info.getDoctitlename());
        docHosipital.setText(info.getDochosp());
        docSpecialty.setText(info.getDocexpert());
    }

    private void getOrderInfo(OrderDetailInfo info){
        doctorPay.setText(info.getUserorderdprice()+"元");
        hosipitalInfo.setText(info.getHospname());
        hosipitalPay.setText(info.getUserorderhprice()+"元");
        time.setText(info.getUserorderstime());
        int pay= info.getUserorderdprice()+info.getUserorderhprice();
        allPay.setText(pay+"元");
    }

    private void loadImage(CircleImageView imageView,int doctorId){
        Uri uri= Uri.parse(IMAGEURI+doctorId);
        Glide.with(getContext())
                .load(uri)
                .error(R.drawable.defaultuserimage)
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .into(imageView);
    }

    public static void actionStart(Context context,int loginId,int orderId,int state){
        Intent intent= new Intent(context,OrderDetail.class);
        intent.putExtra("loginId",loginId);
        intent.putExtra("orderId",orderId);
        intent.putExtra("state",state);
        context.startActivity(intent);
    }

}
