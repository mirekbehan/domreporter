package com.domreporter.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.domreporter.db.HtmlSnapshotSaver;
import com.domreporter.model.HtmlSnapshot;




@Path("/dom")
public class SnapshotService {
	/**
	 * Using Jersey implementation of JAX-RS for consuming requests. This method saves posted page snapshots.
	 * @param uuid uuid (guid) string that is unique for each user session
	 * @param url a url of the page
	 * @param html html of the page
	 * @return confirmation message
	 */
	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_XHTML_XML)
	public String postDom(@QueryParam("uuid") String uuid, @QueryParam("url") String url, String html){
		HtmlSnapshot snapshot = new HtmlSnapshot();
		snapshot.setDom(html);
		snapshot.setPageSessionId(uuid);
		snapshot.setUrl(url);
		HtmlSnapshotSaver dao = new HtmlSnapshotSaver();
		dao.saveDocumentSnapshot(snapshot);
		return "Snapshot received.";
	}
}
