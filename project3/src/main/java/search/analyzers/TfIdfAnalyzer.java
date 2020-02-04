package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
* This class is responsible for computing how "relevant" any given document is
* to a given search query.
*
* See the spec for more details.
*/
public class TfIdfAnalyzer {
	// This field must contain the IDF score for every single word in all
	// the documents.
	private IDictionary<String, Double> idfScores;
	private IDictionary<URI, Double> normDocumentVectors;
	
	// This field must contain the TF-IDF vector for each webpage you were given
	// in the constructor.
	//
	// We will use each webpage's page URI as a unique key.
	private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;
	
	// Feel free to add extra fields and helper methods.
	
	public TfIdfAnalyzer(ISet<Webpage> webpages) {
		// Implementation note: We have commented these method calls out so your
		// search engine doesn't immediately crash when you try running it for the
		// first time.
		//
		// You should uncomment these lines when you're ready to begin working
		// on this class.
		
		normDocumentVectors = new ChainedHashDictionary<>();
		this.idfScores = this.computeIdfScores(webpages);
		this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
	}
	
	// Note: this method, strictly speaking, doesn't need to exist. However,
	// we've included it so we can add some unit tests to help verify that your
	// constructor correctly initializes your fields.
	public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
		return this.documentTfIdfVectors;
	}
	
	// Note: these private methods are suggestions or hints on how to structure your
	// code. However, since they're private, you're not obligated to implement exactly
	// these methods: Feel free to change or modify these methods if you want. The
	// important thing is that your 'computeRelevance' method ultimately returns the
	// correct answer in an efficient manner.
	
	/**
	* This method should return a dictionary mapping every single unique word found
	* in any documents to their IDF score.
	*/
	private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
		int totalPages = pages.size();
		IDictionary<String, Double> result = new ChainedHashDictionary<String, Double>();
		for (Webpage page : pages) {
			ISet<String> markers = new ChainedHashSet<>();
			for (String word : page.getWords()) {
				markers.add(word);
			}
			
			for (String word : markers) {
				if (!result.containsKey(word)) {
					result.put(word, 1.0);
				} else {
					Double value = result.get(word);
					result.put(word, ++value);
				}
			}
		}
		
		for (KVPair<String, Double> pair : result) {
			String key = pair.getKey();
			Double value = pair.getValue();
			value = Math.log(totalPages / value);
			result.put(key, value);
		}
		
		return result;
	}
	
	/**
	* Returns a dictionary mapping every unique word found in the given list
	* to their term frequency (TF) score.
	*
	* We are treating the list of words as if it were a document.
	*/
	private IDictionary<String, Double> computeTfScores(IList<String> words) {
		int totalWords = words.size();
		IDictionary<String, Double> result = new ChainedHashDictionary<String, Double>();
		for (String word : words) {
			if (!result.containsKey(word)) {
				result.put(word, 1.0);
			} else {
				Double tf = result.get(word);
				result.put(word, ++tf);
			}
		}
		
		for (KVPair<String, Double> pair : result) {
			String key = pair.getKey();
			Double value = pair.getValue();
			value = value / totalWords;
			result.put(key, value);
		}
		return result;
	}
	
	/**
	* See spec for more details on what this method should do.
	*/
	private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
		IDictionary<URI, IDictionary<String, Double>> result =
		new ChainedHashDictionary<URI, IDictionary<String, Double>>();
		idfScores = computeIdfScores(pages);
		for (Webpage page : pages) {
			double normDocumentVector = 0.0;
			IDictionary<String, Double> tfScores = computeTfScores(page.getWords());
			for (KVPair<String, Double> pair : tfScores) {
				String key = pair.getKey();
				Double value = pair.getValue();
				value = value * idfScores.get(key);
				tfScores.put(key, value);
				normDocumentVector += value * value;
			}
			result.put(page.getUri(), tfScores);
			normDocumentVectors.put(page.getUri(), Math.sqrt(normDocumentVector));
		}
		
		return result;
	}
	
	/**
	* Returns the cosine similarity between the TF-IDF vector for the given query and the
	* URI's document.
	*
	* Precondition: the given uri must have been one of the uris within the list of
	*               webpages given to the constructor.
	*/
	public Double computeRelevance(IList<String> query, URI pageUri) {
		IDictionary<String, Double> documentVector = documentTfIdfVectors.get(pageUri);
		IDictionary<String, Double> queryVector = new ChainedHashDictionary<String, Double>();
		IDictionary<String, Double> queryTfScores = computeTfScores(query);
		double normQueryVector = 0.0;
		
		for (KVPair<String, Double> pair : queryTfScores) {
			String key = pair.getKey();
			Double value = pair.getValue();
			if (!idfScores.containsKey(key)) {
				queryVector.put(key, 0.0);
			} else {
				double score = idfScores.get(key) * value;
				queryVector.put(key, score);
				normQueryVector += score * score;
			}
		}
		
		normQueryVector = Math.sqrt(normQueryVector);
		
		double numerator = 0.0;
		double docWordScore;
		double queryWordScore;
		for (String word : query) {
			if (documentVector.containsKey(word)) {
				docWordScore = documentVector.get(word);
			} else {
				docWordScore = 0.0;
			}
			
			queryWordScore = queryVector.get(word);
			
			numerator += docWordScore * queryWordScore;
		}
		
		double denominator = normDocumentVectors.get(pageUri) * normQueryVector;
		
		if (denominator != 0) {
			return numerator / denominator;
		} else {
			return 0.0;
		}
	}
}
