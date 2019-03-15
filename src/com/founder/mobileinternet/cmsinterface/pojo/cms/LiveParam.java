package com.founder.mobileinternet.cmsinterface.pojo.cms;

/**
 *
 * Created by codingnuts on 2017/5/15.
 */
public class LiveParam {
	private String t;
	private String sign;
	private String stream_id;
	private int event_type;

	private String app;
	private String app_name;

	//event_type=100;
	private String duration;
	private String file_format;
	private String file_id;
	private String file_size;
	private String video_id;
	private String video_url;
	private long start_time;
	private long end_time;

	private String pic_url;
	private long create_time;

	private String json;

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getStream_id() {
		return stream_id;
	}

	public void setStream_id(String stream_id) {
		this.stream_id = stream_id;
	}

	public int getEvent_type() {
		return event_type;
	}

	public void setEvent_type(int event_type) {
		this.event_type = event_type;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getVideo_id() {
		return video_id;
	}

	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}

	public long getStart_time() {
		return start_time;
	}

	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}

	public long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getFile_format() {
		return file_format;
	}

	public void setFile_format(String file_format) {
		this.file_format = file_format;
	}

	public String getFile_id() {
		return file_id;
	}

	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}

	public String getFile_size() {
		return file_size;
	}

	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}

	@Override
	public String toString() {
		return "LiveParam{" +
				"t='" + t + '\'' +
				", sign='" + sign + '\'' +
				", stream_id='" + stream_id + '\'' +
				", event_type=" + event_type +
				", app='" + app + '\'' +
				", app_name='" + app_name + '\'' +
				", duration='" + duration + '\'' +
				", file_format='" + file_format + '\'' +
				", file_id='" + file_id + '\'' +
				", file_size='" + file_size + '\'' +
				", video_id='" + video_id + '\'' +
				", video_url='" + video_url + '\'' +
				", start_time=" + start_time +
				", end_time=" + end_time +
				", pic_url='" + pic_url + '\'' +
				", create_time=" + create_time +
				", json='" + json + '\'' +
				'}';
	}
}
