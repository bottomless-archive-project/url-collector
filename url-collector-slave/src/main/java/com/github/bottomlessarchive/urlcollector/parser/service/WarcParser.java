package com.github.bottomlessarchive.urlcollector.parser.service;

import com.github.bottomlessarchive.warc.service.WarcFormatException;
import com.github.bottomlessarchive.warc.service.WarcParsingException;
import com.github.bottomlessarchive.warc.service.WarcReader;
import com.github.bottomlessarchive.warc.service.content.domain.WarcContentBlock;
import com.github.bottomlessarchive.warc.service.record.domain.WarcRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarcParser {

    private final ParsingContextFactory parsingContextFactory;
    private final SourceLocationParser sourceLocationParser;

    public Set<String> parseWarcFile(final InputStream warcContent) {
        try {
            final WarcReader warcReader = new WarcReader(warcContent);
            final Set<String> urlsInWorkUnit = new HashSet<>();

            Optional<WarcRecord<WarcContentBlock>> optionalWarcRecord;
            do {
                optionalWarcRecord = readNextRecord(warcReader);

                urlsInWorkUnit.addAll(
                        optionalWarcRecord
                                .map(this::parseWarcRecord)
                                .orElse(Collections.emptySet())
                );
            } while (optionalWarcRecord.isPresent());

            return urlsInWorkUnit;
        } catch (final Exception e) {
            log.error("Failed to crawl urls!", e);

            return Collections.emptySet();
        }
    }

    private Optional<WarcRecord<WarcContentBlock>> readNextRecord(final WarcReader warcReader) {
        try {
            return warcReader.readRecord();
        } catch (final WarcFormatException e) {
            log.debug("Unable to parse warc file: " + e.getMessage());

            return readNextRecord(warcReader);
        }
    }

    private Set<String> parseWarcRecord(final WarcRecord<WarcContentBlock> warcRecord) {
        try {
            return Stream.of(warcRecord)
                    .filter(WarcRecord::isResponse)
                    .map(parsingContextFactory::buildParsingContext)
                    .flatMap(sourceLocationParser::parseLocations)
                    .collect(Collectors.toSet());
        } catch (final WarcParsingException e) {
            log.debug("Unable to parse warc file: " + e.getMessage());

            return Collections.emptySet();
        }
    }
}
