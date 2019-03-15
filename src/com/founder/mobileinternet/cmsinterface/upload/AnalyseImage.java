package com.founder.mobileinternet.cmsinterface.upload;

import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.fileupload.disk.DiskFileItem;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.TrackBox;

public class AnalyseImage
{
	public static String[] getImageResolution(DiskFileItem file)
	{
		String[] result = new String[]
		{ "-1", "-1", "0" };

		String[] fileFullNames = file.getName().split("\\.");
		String fileExtName = null;
		if (fileFullNames.length > 1)
		{
			fileExtName = fileFullNames[fileFullNames.length - 1];

			if (fileExtName.equalsIgnoreCase("jpg") || fileExtName.equalsIgnoreCase("jpeg") || fileExtName.equalsIgnoreCase("png") || fileExtName.equalsIgnoreCase("bmp") || fileExtName.equalsIgnoreCase("gif"))
			{
				try
				{
					Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(fileExtName);
					ImageReader reader = readers.next();
					ImageInputStream iis = ImageIO.createImageInputStream(file.getStoreLocation());
					reader.setInput(iis, true);
					result[0] = String.valueOf(reader.getWidth(0));
					result[1] = String.valueOf(reader.getHeight(0));
					iis.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			} else if (fileExtName.equalsIgnoreCase("mp4"))
			{
				try
				{
					@SuppressWarnings("resource")
					IsoFile isoFile = new IsoFile(file.getStoreLocation().getAbsolutePath());
					MovieBox moov = isoFile.getMovieBox();
					for (Box b : moov.getBoxes())
					{
						if (TrackBox.TYPE.equals(b.getType()))
						{
							TrackBox tb = (TrackBox) b;
							if (tb.getTrackHeaderBox().getWidth() == 0 && tb.getTrackHeaderBox().getHeight() == 0)
								continue;

							result[0] = String.valueOf(tb.getTrackHeaderBox().getWidth());
							result[1] = String.valueOf(tb.getTrackHeaderBox().getHeight());
							break;
						}
					}
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		result[2] = String.valueOf(file.getSize());
		return result;
	}
}
