package cz.hel.osm;

import java.util.ArrayList;
import java.util.List;

import crosby.binary.BinaryParser;
import crosby.binary.Osmformat.DenseNodes;
import crosby.binary.Osmformat.HeaderBlock;
import crosby.binary.Osmformat.Node;
import crosby.binary.Osmformat.Relation;
import crosby.binary.Osmformat.Way;

public class WayParser extends BinaryParser {
	
	final String key;
	final String keySpec;
	final List<List<Long>> nodeIds;
	
	public WayParser(String key, String keySpec) {
		super();
		this.key = key;
		this.keySpec = keySpec;
		this.nodeIds = new ArrayList<>();
	}

	@Override
	protected void parseWays(List<Way> ways) {

		for (Way w : ways) {
			boolean found = false;
			for (int i = 0; i < w.getKeysCount(); i++) {
				String wKey = getStringById(w.getKeys(i));
				String wVal = getStringById(w.getVals(i));

				if (wKey.equals(key) && wVal.equals(keySpec)) {
					found = true;
					break;
				}
			}

			if (found) {
				List<Long> nodesToFind = new ArrayList<>();
				nodeIds.add(nodesToFind);
				long lastRef = 0;
				for (Long ref : w.getRefsList()) {
					lastRef += ref;
					nodesToFind.add(lastRef);
				}
			}
		}
	}

	@Override
	protected void parse(HeaderBlock header) {
	}
	@Override
	public void complete() {
	}
	@Override
	protected void parseRelations(List<Relation> rels) {
	}
	@Override
	protected void parseDense(DenseNodes nodes) {
	}
	@Override
	protected void parseNodes(List<Node> nodes) {
	}
}