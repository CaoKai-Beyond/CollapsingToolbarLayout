package com.weibo.internationa;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private AppBarLayout actAppBar;
    private ProfileMaskForegroundImageView profileHeaderBg;
    private ImageView    profileHeaderAvatar;
    private TextView     profileTitleUser;
    private TextView     profileTitleCount;
    private LinearLayout profileTitleContainer;
    private LinearLayoutManager LinearLayoutManager;
    private FloatingActionButton act_article_scroll2top;
    private Bitmap defaultBlurProfileBitmap;
    private ImageView  profilePersonVerified;
    private Toolbar     mToolbar;
    private FrameLayout mHeaderLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setTranslucent(this,0);

        mRecyclerView=findViewById(R.id.act_profile_recycler);
        actAppBar=findViewById(R.id.appBar);
        profileHeaderBg=findViewById(R.id.profile_header_bg);
        profileHeaderAvatar=findViewById(R.id.profile_header_avatar);
        profileTitleUser=findViewById(R.id.profile_title_user);
        profileTitleCount=findViewById(R.id.profile_title_count);
        profileTitleContainer=findViewById(R.id.profile_title_container);
        act_article_scroll2top=findViewById(R.id.act_article_scroll2top);
        profilePersonVerified=findViewById(R.id.profile_person_verified);
        mToolbar=findViewById(R.id.toolbar);
        mHeaderLayout=findViewById(R.id.header_layout);

        act_article_scroll2top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.scrollToPosition(0);
                actAppBar.setExpanded(true);
            }
        });

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LinearLayoutManager=new LinearLayoutManager(this);
        LinearLayoutManager.setOrientation(android.support.v7.widget.LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(LinearLayoutManager);
        ItemAdapter itemAdapter=new ItemAdapter(this);
        mRecyclerView.setAdapter(itemAdapter);
        setDefaultProfileBg(R.drawable.drawer_bg_4);
        initListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public  int dip2px(float f) {
        return (int) (getResources().getDisplayMetrics().density * f);
    }

    private void setDefaultProfileBg(int i) {
        try {
            this.profileHeaderBg.setImageResource(i);
            if (defaultBlurProfileBitmap == null) {
                int width=getResources().getDisplayMetrics().widthPixels;
               defaultBlurProfileBitmap = Blur.GaussianBlur(this, Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), i),
                       width / 15, dip2px(275.0f) / 15, false), 25);
            }
            if (defaultBlurProfileBitmap != null) {
                this.profileHeaderBg.setForeground(new BitmapDrawable(defaultBlurProfileBitmap));
                this.profileHeaderBg.setForegroundAlpha(0.0f);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void initListener() {
        final float parallaxMultiplier = ((CollapsingToolbarLayout.LayoutParams) this.profileHeaderBg.getLayoutParams()).getParallaxMultiplier();
        this.actAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean collapsed = false;
            boolean titleShow = false;
            ValueAnimator valueAnimator = null;
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                animalOnTitle(i, totalScrollRange);
                animalOnAvatar(i, totalScrollRange);
                animalOnBg(i, totalScrollRange);
            }

            private void animalOnBg(int i, int i2) {
                final float abs = ((float) Math.abs(i)) / ((float) i2);
                profileHeaderBg.post(new Runnable() {
                    public void run() {
                        profileHeaderBg.setForegroundAlpha(abs);
                    }
                });
            }

            private void animalOnAvatar(int i, int i2) {
                float f = (float) i2;
                if (((int) (parallaxMultiplier * f)) + i <= 0 && !this.collapsed) {
                    LogUtil.d("上划进行折叠");
                    this.collapsed = true;
                    mHeaderLayout.animate().cancel();
                    mHeaderLayout.animate().scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setDuration(200).setInterpolator(
                            new FastOutLinearInInterpolator()).setListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                            if (collapsed) {
                                mHeaderLayout.setVisibility(View.INVISIBLE);
                            }
                        }
                    }).start();
                } else if (((int) (f * parallaxMultiplier)) + i >= 0 && this.collapsed) {
                    LogUtil.d("下滑进行展开");
                    this.collapsed = false;
                    mHeaderLayout.setVisibility(View.VISIBLE);
                    mHeaderLayout.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(200).setInterpolator(
                            new LinearOutSlowInInterpolator()).setListener(new AnimatorListenerAdapter() {
                    }).start();
                }
            }

            private void animalOnTitle(int i, int i2) {
                if (this.valueAnimator == null) {
                    this.valueAnimator = ValueAnimator.ofFloat(1.0f, 0.0f).setDuration(300);
                    this.valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            float floatValue = (Float) valueAnimator.getAnimatedValue();
                            profileTitleUser.setTranslationY(((float)profileTitleUser.getHeight()) * floatValue);
                            profileTitleCount.setTranslationY(((float) profileTitleUser.getHeight()) * floatValue);
                            float f = 1.0f - (floatValue * 0.2f);
                            profileTitleUser.setAlpha(f);
                            profileTitleCount.setAlpha(f);
                        }
                    });
                }
                if (i2 + i == 0) {
                    LogUtil.d("完全合并");
                    if (!this.titleShow) {
                        this.titleShow = true;
                        this.valueAnimator.cancel();
                        this.valueAnimator.removeAllListeners();
                        this.valueAnimator.addListener(new AnimatorListenerAdapter() {
                            public void onAnimationStart(Animator animator) {
                                super.onAnimationStart(animator);
                                profileTitleUser.setTranslationY((float) (profileTitleUser.getHeight() / 2));
                                profileTitleUser.setAlpha(0.2f);
                                profileTitleCount.setTranslationY((float) (profileTitleUser.getHeight() / 2));
                                profileTitleCount.setAlpha(0.2f);
                                profileTitleContainer.setVisibility(View.VISIBLE);
                            }

                            public void onAnimationEnd(Animator animator) {
                                super.onAnimationEnd(animator);
                                profileTitleContainer.setVisibility(View.VISIBLE);
                            }
                        });
                        this.valueAnimator.start();
                    }
                } else if (this.titleShow) {
                    this.titleShow = false;
                    this.valueAnimator.cancel();
                    this.valueAnimator.removeAllListeners();
                    this.valueAnimator.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationStart(Animator animator) {
                            profileTitleContainer.setVisibility(View.VISIBLE);
                        }

                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                            profileTitleContainer.setVisibility(View.GONE);
                        }
                    });
                    this.valueAnimator.reverse();
                }
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int i3 = LinearLayoutManager.findFirstVisibleItemPosition() + -1 >= 5 ? View.VISIBLE : View.GONE;
                if (act_article_scroll2top.getVisibility() != i3) {
                    act_article_scroll2top.setVisibility(i3);
                }
            }
        });
    }


}
