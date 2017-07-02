package com.calibreofflinesearcher.dbutil;

import java.util.HashMap;
import java.util.Map;

public class BookInfoForNormalization {

	final int id;
	final String title;
	final String title_na;
	final String author;
	final String author_na;
	final String path;
	final String pubdate;
	final String tags;
	final String tags_na;
	final String description;
	final String description_na;

	BookInfoForNormalization(final int id, final String title, final String author, final String pubdate,
			final String path, final String tags, final String description) {
		this.id = id;
		this.title = title;
		this.title_na = removeAccents(this.title);
		this.author = author;
		this.author_na = removeAccents(this.author);
		this.path = path;
		this.pubdate = pubdate;
		this.tags = tags;
		this.tags_na = removeAccents(this.tags);
		this.description = description;
		this.description_na = removeAccents(this.description);
	}

	/* Android 7 does not have it.
	private static String removeAccents(final String string) {
		final String s = Normalizer.normalize(string, Normalizer.DBNormalizer.NFD);
		final String s2 = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		return s2;
	}
	*/
	private static final Map<Character, Character> NORMALIZED_CHAR_MAP = initNormalizerMap();

	private static Map<Character, Character> initNormalizerMap() {

		Map<Character, Character> normalizedMap = new HashMap<Character, Character>();
		normalizedMap.put('À', 'A');
		normalizedMap.put('Á', 'A');
		normalizedMap.put('Â', 'A');
		normalizedMap.put('Ã', 'A');
		normalizedMap.put('Ä', 'A');
		normalizedMap.put('È', 'E');
		normalizedMap.put('É', 'E');
		normalizedMap.put('Ê', 'E');
		normalizedMap.put('Ë', 'E');
		normalizedMap.put('Í', 'I');
		normalizedMap.put('Ì', 'I');
		normalizedMap.put('Î', 'I');
		normalizedMap.put('Ï', 'I');
		normalizedMap.put('Ù', 'U');
		normalizedMap.put('Ú', 'U');
		normalizedMap.put('Û', 'U');
		normalizedMap.put('Ü', 'U');
		normalizedMap.put('Ű', 'U');
		normalizedMap.put('Ò', 'O');
		normalizedMap.put('Ó', 'O');
		normalizedMap.put('Ô', 'O');
		normalizedMap.put('Õ', 'O');
		normalizedMap.put('Ö', 'O');
		normalizedMap.put('Ő', 'O');
		normalizedMap.put('Ñ', 'N');
		normalizedMap.put('Ç', 'C');
		normalizedMap.put('ª', 'A');
		normalizedMap.put('º', 'O');
		normalizedMap.put('§', 'S');
		normalizedMap.put('³', '3');
		normalizedMap.put('²', '2');
		normalizedMap.put('¹', '1');
		normalizedMap.put('à', 'a');
		normalizedMap.put('á', 'a');
		normalizedMap.put('â', 'a');
		normalizedMap.put('ã', 'a');
		normalizedMap.put('ä', 'a');
		normalizedMap.put('è', 'e');
		normalizedMap.put('é', 'e');
		normalizedMap.put('ê', 'e');
		normalizedMap.put('ë', 'e');
		normalizedMap.put('í', 'i');
		normalizedMap.put('ì', 'i');
		normalizedMap.put('î', 'i');
		normalizedMap.put('ï', 'i');
		normalizedMap.put('ù', 'u');
		normalizedMap.put('ú', 'u');
		normalizedMap.put('û', 'u');
		normalizedMap.put('ü', 'u');
		normalizedMap.put('ű', 'u');
		normalizedMap.put('ò', 'o');
		normalizedMap.put('ó', 'o');
		normalizedMap.put('ô', 'o');
		normalizedMap.put('õ', 'o');
		normalizedMap.put('ö', 'o');
		normalizedMap.put('ő', 'o');
		normalizedMap.put('ñ', 'n');
		normalizedMap.put('ç', 'c');
		return normalizedMap;

	}

	public String removeAccents(final String value) {
		if (value == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder(value);
		for (int i = 0; i < value.length(); i++) {
			Character c = NORMALIZED_CHAR_MAP.get(sb.charAt(i));
			if (c != null) {
				sb.setCharAt(i, c.charValue());
			}
		}
		return sb.toString();
	}
}
