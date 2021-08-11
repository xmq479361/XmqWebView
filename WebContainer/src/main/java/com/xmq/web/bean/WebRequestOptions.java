package com.xmq.web.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/7 18:13
 */
public class WebRequestOptions implements Parcelable {
    public String url;
    public String title;
    public Class<IWebViewInitialer> webViewInitialier;
//    public Class<? extends IJSHandler.JSHandlerDelegate> jsHandler;

    public static Builder newBuilder(String url) {
        return new Builder(url);
    }

    public static final class Builder {
        public String url;
        public String title;
        public Class<IWebViewInitialer> webViewInitialier;
//        public Class<? extends IJSHandler.JSHandlerDelegate> jsHandler;

        private Builder(String url) {
            this.url = url;
        }

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }
//        public Builder withJsHandler(Class<? extends IJSHandler.JSHandlerDelegate>  jsHandler) {
//            this.jsHandler = jsHandler;
//            return this;
//        }
        public Builder withWebViewInitialer(Class<IWebViewInitialer> webViewInitialier) {
            this.webViewInitialier = webViewInitialier;
            return this;
        }

        public WebRequestOptions build() {
            WebRequestOptions webRequestOptions = new WebRequestOptions();
            webRequestOptions.url = this.url;
//            webRequestOptions.jsHandler = this.jsHandler;
            webRequestOptions.webViewInitialier = this.webViewInitialier;
            webRequestOptions.title = this.title;
            return webRequestOptions;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeSerializable(this.webViewInitialier);
//        dest.writeSerializable(this.jsHandler);
    }

    public void readFromParcel(Parcel source) {
        this.url = source.readString();
        this.title = source.readString();
        this.webViewInitialier = (Class<IWebViewInitialer>) source.readSerializable();
//        this.jsHandler = (Class<? extends IJSHandler.JSHandlerDelegate>) source.readSerializable();
    }

    public WebRequestOptions() {
    }

    protected WebRequestOptions(Parcel in) {
        this.url = in.readString();
        this.title = in.readString();
        this.webViewInitialier = (Class<IWebViewInitialer>) in.readSerializable();
//        this.jsHandler = (Class<? extends IJSHandler.JSHandlerDelegate>) in.readSerializable();
    }

    public static final Creator<WebRequestOptions> CREATOR = new Creator<WebRequestOptions>() {
        @Override
        public WebRequestOptions createFromParcel(Parcel source) {
            return new WebRequestOptions(source);
        }

        @Override
        public WebRequestOptions[] newArray(int size) {
            return new WebRequestOptions[size];
        }
    };
}
