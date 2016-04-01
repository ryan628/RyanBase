package ryan.com.librarybase.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import ryan.com.librarybase.R;
import ryan.com.librarybase.utils.Tools;

/**
 * Fresco图片缓存框架View的抽象类，先初始加入，然后每次使用优先使用已有的对象,若有设置其他的功能，则添加方法，若要去掉多余设置则重置
 * FitXY,CenterCrop
 * 用ResizeOptions他就直接缓存处理后大小的图片，如果不用，直接缓存原图，但是原图太大，所以频繁的清除cache。
 * @author notreami
 */
public class MyImageView extends SimpleDraweeView {

	private FrescoMode display = null;
	private FrescoRound round = FrescoRound.NONE;
	private FrescoImage image = FrescoImage.NONE;
	// 圆形，圆角切图～～可复用
	protected RoundingParams roundingParams;
	protected GenericDraweeHierarchyBuilder draweeHierarchyBuilder;
	// 控制～～可复用
	protected PipelineDraweeControllerBuilder draweeControllerBuilder;
	// 图片解码～～可复用
	protected ImageDecodeOptions decodeOptions;

	/**
	 * 图片圆角，切圆以及边框
	 * @author notreami
	 */
	public static enum FrescoRound {
		NONE, CIRCLE, RADIUS, CIRCLEBORDER, RADIUSBORDER
	}

	/**
	 * 图片缩放类型
	 * @author notreami
	 */
	public static enum FrescoMode {
		FitXY, CenterCrop,FIT_CENTER
	}

	/**
	 * 设置默认图片
	 * @author notreami
	 */
	public static enum FrescoImage {
		NONE, NORMAL, CIRCLE,TRANSPAREN , TOPIC , DRYCARGO, WORKTASK
	}

	public MyImageView(Context context, GenericDraweeHierarchy hierarchy) {
		super(context, hierarchy);
		init();
	}

	public MyImageView(Context context) {
		super(context);
		init();
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		decodeOptions = getImageDecodeOptions();// 初始图片解码器
	}

	/**
	 * 修改初始状态
	 * @param display
	 */
	private void modifyFresco(FrescoMode display, FrescoRound round, FrescoImage image) {
		boolean isModify = false;
		if (display != null && (this.display == null || this.display.ordinal() != display.ordinal())) {
			isModify = true;
			this.display = display;
		}
		if (round != null && this.round.ordinal() != round.ordinal()) {
			isModify = true;
			this.round = round;
		}
		if (image != null && this.image.ordinal() != image.ordinal()) {
			isModify = true;
			this.image = image;
		}
		if (isModify) {
			resetBuilder();
		}
	}

	/**
	 * 重置
	 */
	private void resetBuilder() {
		if (round != null&&round!= FrescoRound.NONE)
			roundingParams = getRoundingParams();// 初始切圆
		getGenericDraweeHierarchyBuilder();// 初始化GenericDraweeHierarchyBuilder
		setHierarchy(draweeHierarchyBuilder.build());
	}

	/**
	 * RoundingParams圆形，圆角切图，在运行时，不能改变呈现方式: 原本是圆角，不能改为圆圈。对动图无效
	 */
	protected RoundingParams getRoundingParams() {
		RoundingParams temp = null;
//		 roundingParams.asCircle();//圆形
//		 roundingParams.setBorder(color,width);//fresco:roundingBorderWidth="2dp"边框fresco:roundingBorderColor="@color/border_color"
//		 roundingParams.setCornersRadii(radii);//半径
//		 roundingParams.setCornersRadii(topLeft,topRight,bottomRight,bottomLeft)//fresco:roundTopLeft="true"fresco:roundTopRight="false"fresco:roundBottomLeft="false"fresco:roundBottomRight="true"
//		 roundingParams.setCornersRadius(radius);//fresco:roundedCornerRadius="1dp"圆角
//		 roundingParams.setOverlayColor(overlayColor);//fresco:roundWithOverlayColor="@color/corner_color"
//		 roundingParams.setRoundAsCircle(roundAsCircle);//圆形
//		 roundingParams.setRoundingMethod(roundingMethod);// fresco:progressBarAutoRotateInterval="1000"自动旋转间隔
		if (round != null) {
			switch (round) {
			case CIRCLE:
				temp = RoundingParams.asCircle();//圆形
				break;
			case RADIUS:
				temp = RoundingParams.fromCornersRadius(Tools.dp2px(4));// 半径
				break;
			case CIRCLEBORDER:
				temp = RoundingParams.asCircle();//圆形
				temp.setBorder(getResources().getColor(R.color.c_c8c8cb), Tools.dp2px(1));// fresco:roundingBorderWidth="2dp"边框
				break;
			case RADIUSBORDER:
				temp = RoundingParams.fromCornersRadius(Tools.dp2px(2));
				temp.setBorder(getResources().getColor(R.color.c_c8c8cb), Tools.dp2px(1));// fresco:roundingBorderWidth="2dp"边框
				break;
			case NONE:
			default:
				break;
			}
		}
		return temp;
	}
	/**
	 * GenericDraweeHierarchyBuilder
	 */
	protected void getGenericDraweeHierarchyBuilder() {
		if (draweeHierarchyBuilder == null)
			draweeHierarchyBuilder = new GenericDraweeHierarchyBuilder(getContext().getResources());
		draweeHierarchyBuilder.reset()// 重置
		// .setActualImageColorFilter(colorFilter)// 颜色过滤
		// .setActualImageFocusPoint(focusPoint)//focusCrop, 需要指定一个居中点
		// .setActualImageMatrix(actualImageMatrix)
		// .setActualImageScaleType(actualImageScaleType)//fresco:actualImageScaleType="focusCrop"缩放类型
		// .setBackground(background)//fresco:backgroundImage="@color/blue"背景图片
		// .setBackgrounds(backgrounds)
		   .setFadeDuration(350)// fresco:fadeDuration="150"加载图片动画时间
		// .setFailureImage(failureDrawable)//fresco:failureImage="@drawable/error"失败图
		// .setFailureImage(failureDrawable,failureImageScaleType)//fresco:failureImageScaleType="centerInside"失败图缩放类型
		// .setOverlay(overlay)//fresco:overlayImage="@drawable/watermark"叠加图
		// .setOverlays(overlays)
		// .setPlaceholderImage(placeholderDrawable)//fresco:placeholderImage="@color/wait_color"占位图
		// .setPlaceholderImage(placeholderDrawable,placeholderImageScaleType)//fresco:placeholderImageScaleType="fitCenter"占位图缩放类型
		// .setPressedStateOverlay(drawable)//fresco:pressedStateOverlayImage="@color/red"按压状态下的叠加图
		// .setProgressBarImage(new ProgressBarDrawable())//进度条fresco:progressBarImage="@drawable/progress_bar"进度条
		// .setProgressBarImage(new ProgressBarDrawable(),ScalingUtils.ScaleType.FIT_END)//fresco:progressBarImageScaleType="centerInside"进度条类型
		// .setRetryImage(retryDrawable)//fresco:retryImage="@drawable/retrying"点击重新加载
		// .setRetryImage(retryDrawable,retryImageScaleType)//fresco:retryImageScaleType="centerCrop"点击重新加载缩放类型
		// .setRoundingParams(RoundingParams.asCircle())//圆形/圆角fresco:roundAsCircle="true"圆形
		;
		if (display != null) {
			 switch (display) {
			 case FIT_CENTER:
					draweeHierarchyBuilder.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);// fresco:actualImageScaleType="focusCrop"缩放类型
					break;
			 case FitXY:
				 draweeHierarchyBuilder.setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);// fresco:actualImageScaleType="focusCrop"缩放类型
				 break;
			 case CenterCrop:
			 default:
				 // draweeHierarchyBuilder.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);// fresco:actualImageScaleType="focusCrop"缩放类型
				break;
			 }
			}
		if (image != null) {
			 switch (image) {
			 case NORMAL:
				 draweeHierarchyBuilder
				 .setBackground(getDrawable(R.color.transparent))//fresco:backgroundImage="@color/blue"背景图片
				 .setPlaceholderImage(getDrawable(R.drawable.loading))// fresco:placeholderImageScaleType="fitCenter"占位图缩放类型
				 .setFailureImage(getDrawable(R.drawable.default_img))// fresco:failureImageScaleType="centerInside"失败图缩放类型
				 .setRetryImage(getDrawable(R.drawable.default_img))// fresco:retryImageScaleType="centerCrop"点击重新加载缩放类型
				;
				 break;
			 case TRANSPAREN:
				 draweeHierarchyBuilder
				 .setBackground(getDrawable(R.color.transparent))//fresco:backgroundImage="@color/blue"背景图片
				 .setPlaceholderImage(getDrawable(R.drawable.loading))// fresco:placeholderImageScaleType="fitCenter"占位图缩放类型
				 .setFailureImage(getDrawable(R.drawable.default_img))// fresco:failureImageScaleType="centerInside"失败图缩放类型
				 .setRetryImage(getDrawable(R.drawable.default_img))// fresco:retryImageScaleType="centerCrop"点击重新加载缩放类型
				;
				 break;
				 case TOPIC:
					 draweeHierarchyBuilder
							 .setBackground(getDrawable(R.color.transparent))//fresco:backgroundImage="@color/blue"背景图片
							 .setPlaceholderImage(getDrawable(R.drawable.loading))// fresco:placeholderImageScaleType="fitCenter"占位图缩放类型
							 .setFailureImage(getDrawable(R.drawable.default_img))// fresco:failureImageScaleType="centerInside"失败图缩放类型
							 .setRetryImage(getDrawable(R.drawable.default_img)); // fresco:retryImageScaleType="centerCrop"点击重新加载缩放类型
					 break;
				 case DRYCARGO:
					 draweeHierarchyBuilder
							 .setBackground(getDrawable(R.color.transparent))//fresco:backgroundImage="@color/blue"背景图片
							 .setPlaceholderImage(getDrawable(R.drawable.loading))// fresco:placeholderImageScaleType="fitCenter"占位图缩放类型
							 .setFailureImage(getDrawable(R.drawable.default_img))// fresco:failureImageScaleType="centerInside"失败图缩放类型
							 .setRetryImage(getDrawable(R.drawable.default_img)); // fresco:retryImageScaleType="centerCrop"点击重新加载缩放类型
					 break;
				 case WORKTASK:
					 draweeHierarchyBuilder
							 .setBackground(getDrawable(R.color.transparent))//fresco:backgroundImage="@color/blue"背景图片
							 .setPlaceholderImage(getDrawable(R.drawable.loading))// fresco:placeholderImageScaleType="fitCenter"占位图缩放类型
							 .setFailureImage(getDrawable(R.drawable.default_img))// fresco:failureImageScaleType="centerInside"失败图缩放类型
							 .setRetryImage(getDrawable(R.drawable.default_img)); // fresco:retryImageScaleType="centerCrop"点击重新加载缩放类型
					 break;
			 case NONE:
			 default:
				 draweeHierarchyBuilder
				 .setBackground(getDrawable(R.color.transparent))//fresco:backgroundImage="@color/blue"背景图片
				 .setPlaceholderImage(getDrawable(R.drawable.loading))// fresco:placeholderImageScaleType="fitCenter"占位图缩放类型
				 .setFailureImage(getDrawable(R.drawable.default_img))// fresco:failureImageScaleType="centerInside"失败图缩放类型
				 .setRetryImage(getDrawable(R.drawable.default_img))// fresco:retryImageScaleType="centerCrop"点击重新加载缩放类型
				;
				break;
			 }
			}
		if (round != null&&round!= FrescoRound.NONE) {
			draweeHierarchyBuilder.setRoundingParams(roundingParams);// 圆形/圆角fresco:roundAsCircle="true"圆形
		}
	}

	protected void getPipelineDraweeControllerBuilder() {
		if (draweeControllerBuilder == null)
			draweeControllerBuilder = Fresco.newDraweeControllerBuilder();
		draweeControllerBuilder.reset()// 重置
		.setAutoPlayAnimations(true)// 自动播放图片动画
		// .setCallerContext(callerContext)//回调
		// .setControllerListener(view.getListener())//监听图片下载完毕等
		// .setDataSourceSupplier(dataSourceSupplier)//数据源
		// .setFirstAvailableImageRequests(firstAvailableImageRequests)//本地图片复用，可加入ImageRequest数组
		// .setImageRequest(imageRequest)//设置单个图片请求～～～不可与setFirstAvailableImageRequests共用，配合setLowResImageRequest为高分辨率的图
		// .setLowResImageRequest(ImageRequest.fromUri(lowResUri))//先下载显示低分辨率的图
		.setOldController(getController())// DraweeController复用
		// .setTapToRetryEnabled(true)//点击重新加载图
		;
	}

	/**
	 * 图片解码ImageDecodeOptionsBuilder
	 */
	protected ImageDecodeOptions getImageDecodeOptions() {
		ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder().setBackgroundColor(Color.TRANSPARENT)// 图片的背景颜色
				// .setDecodeAllFrames(true)// 解码所有帧
				// .setDecodePreviewFrame(true)// 解码预览框
				// .setForceOldAnimationCode(getan)//使用以前动画
				// .setFrom(options)//使用已经存在的图像解码
				// .setMinDecodeIntervalMs(intervalMs)//最小解码间隔（分位单位）
				.setUseLastFrameForPreview(true)// 使用最后一帧进行预览
				.build();
		return decodeOptions;
	}

	/**
	 * 图片显示的Builder
	 */
	protected ImageRequestBuilder getImageRequestBuilder(Uri uri) {
		ImageRequestBuilder requestBuilder = ImageRequestBuilder.newBuilderWithSource(uri)
				// .setResizeOptions(new ResizeOptions(getLayoutParams().width, getLayoutParams().height));//设置图片宽高
				.setAutoRotateEnabled(false)// 自动旋转图片方向
				.setImageDecodeOptions(decodeOptions)//   图片解码库
				.setImageType(ImageRequest.ImageType.DEFAULT)// 图片类型，设置后可调整图片放入小图磁盘空间还是默认图片磁盘空间
				.setLocalThumbnailPreviewsEnabled(true)// 缩略图预览，影响图片显示速度（轻微）
				.setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)//请求经过缓存级别BITMAP_MEMORY_CACHE，ENCODED_MEMORY_CACHE，DISK_CACHE，FULL_FETCH
				// .setPostprocessor(postprocessor)//修改图片
				.setProgressiveRenderingEnabled(true)// 渐进加载，主要用于渐进式的JPEG图，影响图片显示速度（普通）
				// .setResizeOptions(new ResizeOptions(view.getLayoutParams().width,view.getLayoutParams().height))//调整大小
				// .setSource(Uri uri)//设置图片地址
				;
		if(getLayoutParams().width>0&&getLayoutParams().height>0){
			requestBuilder.setResizeOptions(new ResizeOptions(getLayoutParams().width, getLayoutParams().height));//设置图片宽高
		}
		return requestBuilder;
	}

	/*************** 加载图片的设置 ******************/
	/**
	 * 单纯放入图片
	 */
	public void loadImg(String uri) {
		if (TextUtils.isEmpty(uri))
			return;
		modifyFresco(FrescoMode.CenterCrop,FrescoRound.RADIUS,FrescoImage.NONE);

		DraweeController controller = null;
		ImageRequest imageRequest = getImageRequestBuilder(Uri.parse(uri)).build();
		getPipelineDraweeControllerBuilder();
		controller = draweeControllerBuilder.setImageRequest(imageRequest).build();
		setController(controller);
	}
	/**
	 * 单纯放入图片
	 */
	public void loadImg(String uri,int type) {
		if (TextUtils.isEmpty(uri))
			return;
		switch (type)
		{
			case 0:
				modifyFresco(FrescoMode.CenterCrop,FrescoRound.RADIUS,FrescoImage.NONE);
				break;
			case 1:
				modifyFresco(FrescoMode.CenterCrop,FrescoRound.NONE,FrescoImage.NONE);
				break;
			case 2:
				modifyFresco(FrescoMode.CenterCrop,FrescoRound.CIRCLE,FrescoImage.NONE);
				break;
		}

		DraweeController controller = null;
		ImageRequest imageRequest = getImageRequestBuilder(Uri.parse(uri)).build();
		getPipelineDraweeControllerBuilder();
		controller = draweeControllerBuilder.setImageRequest(imageRequest).build();
		setController(controller);
	}
	/**
	 * 单纯放入图片
	 */
	public void setImageRequest(String uri,FrescoMode display, FrescoRound round, FrescoImage image) {
		if (TextUtils.isEmpty(uri))
			return;
		modifyFresco(display,round,image);

		DraweeController controller = null;
		ImageRequest imageRequest = getImageRequestBuilder(Uri.parse(uri)).build();
		getPipelineDraweeControllerBuilder();
		controller = draweeControllerBuilder.setImageRequest(imageRequest).build();
		setController(controller);
	}
	/**
	 * 先显示低分辨率的图，然后是高分辨率的图
	 */
	public void setLowResHighRes(String lowResUri, String uri,FrescoMode display, FrescoRound round, FrescoImage image) {
		if (TextUtils.isEmpty(uri))
			return;
		modifyFresco(display,round,image);
		ImageRequest lowimageRequest = getImageRequestBuilder(Uri.parse(lowResUri)).build();
		ImageRequest highimageRequest = getImageRequestBuilder(Uri.parse(uri)).build();
		getPipelineDraweeControllerBuilder();
		DraweeController controller = draweeControllerBuilder.setLowResImageRequest(lowimageRequest).setImageRequest(highimageRequest).build();
		setController(controller);
	}

	/**
	 * 本地图片复用
	 */
	public void setReuse(String uri1, String uri2, FrescoMode display, FrescoRound round, FrescoImage image) {
		if (TextUtils.isEmpty(uri1) || TextUtils.isEmpty(uri2))
			return;
		modifyFresco(display,round,image);
		ImageRequest imageRequest1 = getImageRequestBuilder(Uri.parse(uri1)).build();
		ImageRequest imageRequest2 = getImageRequestBuilder(Uri.parse(uri2)).build();
		ImageRequest[] requests = { imageRequest1, imageRequest2 };
		getPipelineDraweeControllerBuilder();
		DraweeController controller = draweeControllerBuilder.setFirstAvailableImageRequests(requests).build();
		setController(controller);
	}

	/*************** 加载图片后的下载设置 ******************/
	/**
	 * 下载监听
	 */
	public void setImageRequest(String uri, ControllerListener<ImageInfo> controllerListener, FrescoMode display, FrescoRound round, FrescoImage image) {
		if (TextUtils.isEmpty(uri))
			return;
		modifyFresco(display,round,image);

		DraweeController controller = null;
		ImageRequest imageRequest = getImageRequestBuilder(Uri.parse(uri)).build();
		getPipelineDraweeControllerBuilder();
		controller = draweeControllerBuilder.setImageRequest(imageRequest).setControllerListener(controllerListener).build();
		setController(controller);
	}

	/**
	 * 获取内存的Bitmap
	 *
	 * @param uri
	 * @return
	 */
	public DataSource<CloseableReference<CloseableImage>> getTempBitmap(String uri, FrescoMode display, FrescoRound round, FrescoImage image) {
		modifyFresco(display,round,image);
		ImageRequest imageRequest = getImageRequestBuilder(Uri.parse(uri)).build();
		ImagePipeline imagePipeline = Fresco.getImagePipeline();
		DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchImageFromBitmapCache(imageRequest, null);
		return dataSource;
	}

	/*************** 加载图片后的设置 ******************/
	/**
	 * 居中聚焦点
	 */
	public void setImageFocusPoint(PointF focusPoint) {
		getHierarchy().setActualImageFocusPoint(focusPoint);
	}

	/**
	 * 动画图播放／停止~需要先等待图片加载完毕调用
	 */
	public void setAnimatableStart() {
		Animatable animation = getController().getAnimatable();
		if (animation != null) {
			animation.start();// 开始播放
		}
	}

	public void setAnimatableStop() {
		Animatable animation = getController().getAnimatable();
		if (animation != null) {
			if (animation.isRunning())
				animation.stop();// 一段时间之后，根据业务逻辑，停止播放
		}
	}

	@SuppressWarnings("deprecation")
	protected Drawable getDrawable(int rid) {
		return getContext().getResources().getDrawable(rid);
	}
}