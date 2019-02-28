package com.example.lenovo.englishstudy.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.englishstudy.userdefined.FlowLayout;
import com.example.lenovo.englishstudy.SearchHistoryActivity;
import com.example.lenovo.englishstudy.animation.ExplosionField;
import com.example.lenovo.englishstudy.animation.MoveImageView;
import com.example.lenovo.englishstudy.animation.PointFTypeEvaluator;
import com.example.lenovo.englishstudy.R;
import com.example.lenovo.englishstudy.db.Sentence;


public class SearchFragment extends Fragment {

    private ImageView hezi;
    private RelativeLayout contain;
    private Toolbar toolbar;
    private ImageView end;
    private ImageView ianswer;
    private TextView answer1;
    private TextView answer2;
    private String as = "";
    private Boolean flag1 = true;
    private Boolean flag2 = false;
    private Boolean flag3 = false;
    private String mNames[] = {
            "welcome","android","TextView",
            "apple","experience","kobe bryant",
            "jordan","layout","viewgroup",
            "margin","padding","text",
            "name","type","search","logcat"
    };
    private FlowLayout mFlowLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searchfragment,container,false);
        end = view.findViewById(R.id.end);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag3) {
                    flag1 = false;
                    end.setVisibility(View.GONE);
                    answer2.setText(as);
                    Sentence sentence = new Sentence();
                    sentence.setSentence(as);
                    sentence.save();
                    beginAnimation2();
                }
            }
        });
        end.setVisibility(View.GONE);
        answer1 = view.findViewById(R.id.answer1);
        answer1.setVisibility(View.GONE);
        answer2 = view.findViewById(R.id.answer2);
        answer2.setVisibility(View.GONE);
        toolbar = view.findViewById(R.id.title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        ianswer = view.findViewById(R.id.ianswer);
        ianswer.setVisibility(View.GONE);
        contain = view.findViewById(R.id.search);
        hezi = view.findViewById(R.id.hezi);
        initChildViews(view);
        return view;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history:
                Intent intent = new Intent(getActivity(), SearchHistoryActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    public void beginAnimation1(View view) {
        int[] childCoordinate = new int[2];
        int[] parentCoordinate = new int[2];
        int[] shopCoordinate = new int[2];
        //1.分别获取被点击View、父布局、购物车在屏幕上的坐标xy。
        view.getLocationInWindow(childCoordinate);
        contain.getLocationInWindow(parentCoordinate);
        hezi.getLocationInWindow(shopCoordinate);

        Log.d("1234",shopCoordinate[1] + " ");

        //2.自定义ImageView 继承ImageView
        MoveImageView img = new MoveImageView(getContext());
        img.setImageResource(R.mipmap.ic_heart);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(120, 120);//两个400分别为添加图片的大小
        img.setLayoutParams(params);

        //3.设置img在父布局中的坐标位置
        img.setX(childCoordinate[0] - parentCoordinate[0] );
        img.setY(childCoordinate[1] - parentCoordinate[1] );
        Log.d("12345",shopCoordinate[1] + " ");
        //4.父布局添加该Img
        contain.addView(img);

        //5.利用 二次贝塞尔曲线 需首先计算出 MoveImageView的2个数据点和一个控制点
        PointF startP = new PointF();
        PointF endP = new PointF();
        PointF controlP = new PointF();
        //开始的数据点坐标就是 addV的坐标
        startP.x = childCoordinate[0] - parentCoordinate[0] ;
        startP.y = childCoordinate[1] - parentCoordinate[1];
        //结束的数据点坐标就是 shopImg的坐标
        endP.x = shopCoordinate[0] - parentCoordinate[0] + 150;
        endP.y = shopCoordinate[1] - parentCoordinate[1] + 80;
        //控制点坐标 x等于 购物车x；y等于 addV的y
        controlP.x = endP.x;
        controlP.y = startP.y;
        Log.d("123456",shopCoordinate[1] + " ");

        //启动属性动画
        ObjectAnimator animator = ObjectAnimator.ofObject(img, "mPointF",
                new PointFTypeEvaluator(controlP), startP, endP);
        animator.setDuration(1000);
        Log.d("1234567",shopCoordinate[1] + " ");
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束后 父布局移除 img
                Object target = ((ObjectAnimator) animation).getTarget();
                contain.removeView((View) target);
                //shopImg 开始一个放大动画
                Animation scaleAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim);
                hezi.startAnimation(scaleAnim);
                scaleAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        flag3 = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void beginAnimation2() {
        Animation rotateAnimation = new RotateAnimation(0, 5, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new CycleInterpolator(5));
        rotateAnimation.setDuration(1000);
        hezi.startAnimation(rotateAnimation);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setSelfAndChildDisappearOnClick(hezi);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    /**
     * 为自己以及子View添加破碎动画，动画结束后，把View消失掉
     * @param view 可能是ViewGroup的view
     */
    private void setSelfAndChildDisappearOnClick(final View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                setSelfAndChildDisappearOnClick(viewGroup.getChildAt(i));
            }
        } else {

            new ExplosionField(SearchFragment.this.getContext()).explode(view,
                    new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setVisibility(View.GONE);
                            answer1.setVisibility(View.VISIBLE);
                            ianswer.setVisibility(View.VISIBLE);
                            answer2.setVisibility(View.VISIBLE);
                            flag2 = true;
                            flag1 = true;
                            as = "";
                        }
                    });
        }

    }

    private void initChildViews(View view) {
        // TODO Auto-generated method stub
        mFlowLayout = (FlowLayout) view.findViewById(R.id.flowlayout);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.topMargin = 10;
        lp.bottomMargin = 10;
        for(int i = 0; i < mNames.length; i ++){
            final TextView textView = new TextView(getContext());
            textView.setText(mNames[i]);
            textView.setTextColor(Color.BLACK);
            textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview));
            mFlowLayout.addView(textView,lp);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flag3 = false;
                    as += textView.getText() + " ";
                    if(flag1) {
                        beginAnimation1(view);
                        end.setVisibility(View.VISIBLE);
                    }
                    if(flag2) {
                        hezi.setVisibility(View.VISIBLE);
                        answer1.setVisibility(View.GONE);
                        answer2.setVisibility(View.GONE);
                        ianswer.setVisibility(View.GONE);
                    }
                }
            });
        }

    }

}




