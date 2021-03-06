package sample.server;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import sample.client.play.service.PlayService;
import sample.shared.PlayDisplay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class PlayServiceImpl extends RemoteServiceServlet implements
        PlayService {

	public String greetServer(String input) throws IllegalArgumentException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity employee = new Entity("Employee", "asalieri");
		datastore.put(employee);
		return "";
	}

	@Override
	public PlayDisplay registerPlay(PlayDisplay d) {
		// define entity
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity playEntity = new Entity(PlayDisplay.class.getName());
		playEntity.setProperty("hit", d.getHit());
		playEntity.setProperty("player", d.getPlayer());
		playEntity.setProperty("playDate", d.getPlayDate());
		// save
		Key key = datastore.put(playEntity);
		return d;
	}
	
	@Override
	public List<PlayDisplay> readPlays() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query(PlayDisplay.class.getName());
		List<PlayDisplay> displays = executeQuery(datastore, query);
		return displays;
	}

	@Override
	public List<PlayDisplay> filterByPlayer(String player) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query(PlayDisplay.class.getName());
		query.setFilter(new Query.FilterPredicate("player", FilterOperator.EQUAL, player));
		List<PlayDisplay> displays = executeQuery(datastore, query);
		return displays;
	}

	/**
	 * @param datastore
	 * @param query
	 * @return
	 */
	private List<PlayDisplay> executeQuery(DatastoreService datastore,
			Query query) {
		List<Entity> asList = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));
		List<PlayDisplay> displays = new ArrayList<PlayDisplay>();
		for (Entity e : asList) {
			PlayDisplay d = new PlayDisplay();
			d.setPlayDate((Date) e.getProperty("playDate"));
			d.setHit(((Long) e.getProperty("hit")).intValue());
			d.setPlayer((String) e.getProperty("player"));
			displays.add(d);
		}
		return displays;
	}
	
	
}
