package search.analyzers;

import java.net.URI;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import search.models.Webpage;

public class EnhancedQueryAnalyzer {

	private IDictionary<URI, Double> pageRanks;
	

	public EnhancedQueryAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
		
		IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);
		

		this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

	}
	
	
	private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
		
		IDictionary<URI, ISet<URI>> result = new ChainedHashDictionary<URI, ISet<URI>>();
		ISet<URI> inclusivenessCheck = new ChainedHashSet<URI>();
		
		for (Webpage page : webpages) {
			inclusivenessCheck.add(page.getUri());
		}
		
		for (Webpage page : webpages) {
			URI currentPageUri = page.getUri();
			result.put(currentPageUri, new ChainedHashSet<URI>());
			for (URI link : page.getLinks()) {
				if (!link.equals(currentPageUri) && inclusivenessCheck.contains(link)) {
					result.get(currentPageUri).add(link);
				}
			}
		}
		return result;
	}
	

}
