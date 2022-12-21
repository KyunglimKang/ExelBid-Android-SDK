# 네이티브 템플릿 광고 적용 가이드
#### 1. 광고 적용을 원하는 layout 위치에 제공받은 네이티브 템플릿 layout 을 세팅합니다. (exelbid_native_template_***.xml 파일 제공)
#### 2. 해당 layout 에 필요한 요소들을 세팅합니다. (drawable, font, style)
#### 3. 사용할 네이티브 템플릿의 최상단 레이아웃 id 를 통해 뷰 객체를 가져옵니다.
```java
// 예시1) 320_50_without_chatbox.xml
mNativeRootLayout = view.findViewById(R.id.root_small_without_chatbox);

// 예시2) 320_100_chatbox.xml
mNativeRootLayout = view.findViewById(R.id.root_medium_chatbox);

// 예시3) 320_480_animated.xml
mNativeRootLayout = view.findViewById(R.id.root_large_animated);
```
#### 4. 네이티브 광고를 적용합니다.
   
   [엑셀비드 SDK 네이티브 광고 적용 가이드](https://github.com/onnuridmc/ExelBid-Android-SDK#%EB%84%A4%EC%9D%B4%ED%8B%B0%EB%B8%8C)와 아래 [예시 코드](#광고-적용-예시-코드)를 참고하여 광고를 적용합니다.
        
   - 애니메이션 템플릿을 적용하는 경우, 다음 샘플 코드를 광고가 로딩된 시점(onLoaded)에 애니메이션을 적용합니다.
   
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
### 광고 적용 예시 코드

- 예시1) 320_50_without_chatbox.xml 템플릿 적용
```java
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
// *** 템플릿 별 지원하는 layout 요소, 바인딩, 요청 asset 정보가 일치해야함.  
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
```

- 예시2) 320_100_chatbox.xml 템플릿 적용
```java
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
```
- 예시3) 320_480_animated.xml 템플릿 적용
```java
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

        // 7. 애니메이션을 적용합니다.
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
```
