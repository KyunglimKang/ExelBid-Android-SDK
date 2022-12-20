### 320*50 네이티브 템플릿 광고 적용
- loadNativeSmallSample 함수를 생성한 뒤, activity 에서 해당 함수를 호출합니다.
```java
public void loadNativeSmallSample(Context context, String unitId){

        // 1. 네이티브 광고 인스턴스를 생성합니다
        mNativeAd = new ExelBidNative(context, unitId, new OnAdNativeListener() {
            @Override
            public void onFailed(ExelBidError error, int statusCode) {
                Toast.makeText(context,"광고 없음", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onShow() {
            }
            @Override
            public void onClick() {
            }
            @Override
            public void onLoaded() {
                // 서버로부터 광고를 가져왔을 경우에 호출됩니다.
                // 6. 네이티브 광고를 노출합니다.
                mNativeAd.show();
            }
        });

        // 2. 광고가 노출될 영역에 대한 정보를 바인딩 합니다.
        nativeViewBinder = new NativeViewBinder.Builder(nativeRootLayout)
                .mainImageId(R.id.native_main_image)
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .iconImageId(R.id.native_icon_image)
                .adInfoImageId(R.id.native_privacy_information_icon_image)
                .build();
        mNativeAd.setNativeViewBinder(nativeViewBinder);

        // 3. 네이티브 광고 요청 시, 어플리케이션에서 필수로 요청할 항목들을 설정합니다.
        mNativeAd.setRequiredAsset(new NativeAsset[] {NativeAsset.TITLE, NativeAsset.ICON, NativeAsset.MAINIMAGE});
        
        // 4. 네이티브 광고를 요청합니다.
        mNativeAd.loadAd();
    }
```

- 광고가 로딩된 시점(onLoaded)에 애니메이션을 적용합니다.
```java
// cta 버튼 화살표 애니메이션
ImageView arrow_btn = (ImageView) nativeRootLayout.findViewById(R.id.arrow_btn);
ObjectAnimator translationX = ObjectAnimator.ofFloat(arrow_btn, "translationX", -5f, 10f);
translationX.setDuration(450);
translationX.setRepeatCount(ValueAnimator.INFINITE);
translationX.setRepeatMode(ValueAnimator.REVERSE);
translationX.start();

// "touch me" 말풍선 애니메이션
TextView chatbox = (TextView) nativeRootLayout.findViewById(R.id.chatbox);
ObjectAnimator scaleXUpAnim = ObjectAnimator.ofFloat(chatbox, "scaleX", 0f, 1f);
ObjectAnimator scaleYUpAnim = ObjectAnimator.ofFloat(chatbox, "scaleY", 0f, 1f);
ObjectAnimator scaleXLittleUpAnim = ObjectAnimator.ofFloat(chatbox, "scaleX", 1f, 1.2f);
ObjectAnimator scaleYLittleUpAnim = ObjectAnimator.ofFloat(chatbox, "scaleY", 1f, 1.2f);
ObjectAnimator scaleXDownAnim = ObjectAnimator.ofFloat(chatbox, "scaleX", 1.2f, 0f);
ObjectAnimator scaleYDownAnim = ObjectAnimator.ofFloat(chatbox, "scaleY", 1.2f, 0f);

AnimatorSet scaleUpSet = new AnimatorSet();
scaleUpSet.playTogether(scaleXUpAnim, scaleYUpAnim);
scaleUpSet.setDuration(500);

AnimatorSet scaleLittleUpSet = new AnimatorSet();
scaleLittleUpSet.playTogether(scaleXLittleUpAnim, scaleYLittleUpAnim);
scaleLittleUpSet.setStartDelay(2000);
scaleLittleUpSet.setDuration(200);

AnimatorSet scaleDownSet = new AnimatorSet();
scaleDownSet.playTogether(scaleXDownAnim, scaleYDownAnim);
scaleDownSet.setDuration(500);

AnimatorSet scaleSet = new AnimatorSet();

scaleSet.play(scaleDownSet).after(scaleLittleUpSet).after(scaleUpSet);
scaleSet.start();

scaleSet.addListener(new Animator.AnimatorListener() {
    private boolean mAnimationStop;

    @Override
    public void onAnimationStart(Animator animator) {
        mAnimationStop = false;
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        if(!mAnimationStop){
            scaleSet.setStartDelay(2000);
            scaleSet.start();
        }
    }

    @Override
    public void onAnimationCancel(Animator animator) {
        mAnimationStop = true;
    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
});
```

### 320*100 네이티브 템플릿 광고 적용
- loadNativeSmallSample 함수를 생성한 뒤, activity 에서 해당 함수를 호출합니다.
```java
public void loadNativeSample(Context context, String unitId){
        // 1. 네이티브 광고 인스턴스를 생성합니다
        mNativeAd = new ExelBidNative(context, unitId, new OnAdNativeListener() {
            @Override
            public void onFailed(ExelBidError error, int statusCode) {
            }
            @Override
            public void onShow() {
            }
            @Override
            public void onClick() {
            }
            @Override
            public void onLoaded() {
                // 서버로부터 광고를 가져왔을 경우에 호출됩니다.
                // 6. 네이티브 광고를 노출합니다.
                mNativeAd.show();
            }
        });
        
        // 2. 광고가 노출될 영역에 대한 정보를 바인딩 합니다.
        nativeViewBinder = new NativeViewBinder.Builder(nativeRootLayout)
                .mainImageId(R.id.native_main_image)
                .callToActionButtonId(R.id.native_cta)
                .titleTextViewId(R.id.native_title)
                .textTextViewId(R.id.native_text)
                .iconImageId(R.id.native_icon_image)
                .adInfoImageId(R.id.native_privacy_information_icon_image)
                .build();
        mNativeAd.setNativeViewBinder(nativeViewBinder);
        
        // 3. 네이티브 광고 요청 시, 어플리케이션에서 필수로 요청할 항목들을 설정합니다.
        mNativeAd.setRequiredAsset(new NativeAsset[] {NativeAsset.TITLE, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});
        
        // 4. 네이티브 광고를 요청합니다.
        mNativeAd.loadAd();
    }
```
