package de.mq.mapping.util.proxy.model;

import java.util.Comparator;

public class VideoAOComparator implements Comparator<VideoAO>{

	@Override
	public int compare(VideoAO video1, VideoAO video2) {
		
		return video1.getTitle().compareToIgnoreCase(video2.getTitle());
	}

}
