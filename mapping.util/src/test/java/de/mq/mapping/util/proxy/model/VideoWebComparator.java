package de.mq.mapping.util.proxy.model;

import java.util.Comparator;

public class VideoWebComparator implements Comparator<VideoWeb>{

	@Override
	public int compare(VideoWeb video1, VideoWeb video2) {
		
		return video1.getTitle().compareToIgnoreCase(video2.getTitle());
	}

}
