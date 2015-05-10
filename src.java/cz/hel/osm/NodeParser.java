package cz.hel.osm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import crosby.binary.BinaryParser;
import crosby.binary.Osmformat.DenseNodes;
import crosby.binary.Osmformat.HeaderBlock;
import crosby.binary.Osmformat.Node;
import crosby.binary.Osmformat.Relation;
import crosby.binary.Osmformat.Way;

public class NodeParser extends BinaryParser {
	
	final Set<Long> nodesToFind;
	final Map<Long, GPSCoordinates> found;
	
	public NodeParser(Set<Long> nodesToFind) {
		super();
		this.nodesToFind = nodesToFind;
		this.found = new HashMap<>();
	}

	@Override
	protected void parseDense(DenseNodes nodes) {
		long lastId = 0;
		long lastLat = 0;
		long lastLon = 0;

		for (int i = 0; i < nodes.getIdCount(); i++) {
			lastId += nodes.getId(i);
			lastLat += nodes.getLat(i);
			lastLon += nodes.getLon(i);

			if (!nodesToFind.contains(lastId)) {
				continue;
			}
			
			found.put(lastId, new GPSCoordinates(parseLat(lastLat), parseLon(lastLon)));
		}
	}
	
	@Override
	protected void parseRelations(List<Relation> rels) {
	}
	@Override
	protected void parseNodes(List<Node> nodes) {
	}
	@Override
	protected void parseWays(List<Way> ways) {
	}
	@Override
	protected void parse(HeaderBlock header) {
	}
	@Override
	public void complete() {
	}
}