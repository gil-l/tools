package pt.utl.ist.fenix.tools.file.dspace;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import pt.utl.ist.fenix.tools.file.AbstractFileManager;
import pt.utl.ist.fenix.tools.file.FileManagerException;
import pt.utl.ist.fenix.tools.file.FileSearchCriteria;
import pt.utl.ist.fenix.tools.file.FileSearchResult;
import pt.utl.ist.fenix.tools.file.FileSet;
import pt.utl.ist.fenix.tools.file.FileSetDescriptor;
import pt.utl.ist.fenix.tools.file.FileSetQueryResults;
import pt.utl.ist.fenix.tools.file.FileSetType;
import pt.utl.ist.fenix.tools.file.FilesetMetadataQuery;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public class DSpaceFileManager extends AbstractFileManager {

    private Logger logger = Logger.getLogger(DSpaceFileManager.class.getName());

    private String downloadUrlFormat;

    protected IDSpaceClient dspaceClient;

    public DSpaceFileManager() {
        super();
        init();
    }

    public DSpaceFileManager(final Properties properties) {
        super(properties);
        init();
    }

    private void init() {
        try {
            downloadUrlFormat = getProperty("dspace.serverUrl") + "/" + getProperty("dspace.downloadUriFormat");
            logger.log(Level.INFO, "Configured downloadUrlFormat " + downloadUrlFormat);
            final String dspaceClientClassName = getProperty("dspace.client.transport.class");
            logger.log(Level.INFO, "DSpace client class name " + dspaceClientClassName);
            dspaceClient = (IDSpaceClient) Class.forName(dspaceClientClassName).newInstance();
            logger.log(Level.INFO, "Initializing dspace client");
            dspaceClient.init(this);
        } catch (InstantiationException e) {
            throw new RuntimeException("Error loading dspace file manager", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error loading dspace file manager", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading dspace file manager", e);
        }
    }

    @Override
    public FileSetDescriptor internalSaveFileSet(VirtualPath virtualPath, String originalFileName, boolean privateFile,
            FileSet fileSet, FileSetType fileSetType) {
        try {
            return dspaceClient.uploadFileSet(virtualPath, originalFileName, fileSet, privateFile);
        } catch (DSpaceClientException e) {
            logger.warning(this.getClass().getName() + ": " + e.getMessage());
            throw new FileManagerException(e.getMessage(), e);
        }
    }

    public String getDirectDownloadUrlFormat() {
        return downloadUrlFormat;
    }

    @Override
    public String formatDownloadUrl(String uniqueId, String fileName) {
        String formatedDownloadUrl = downloadUrlFormat;
        return MessageFormat.format(formatedDownloadUrl, uniqueId, fileName);
    }

    @Override
    public InputStream retrieveFile(String uniqueId) {
        try {
            return dspaceClient.retrieveStreamForFile(uniqueId);
        } catch (DSpaceClientException e) {
            throw new FileManagerException(e.getMessage(), e);
        }
    }

    @Override
    public FileSet readFileSet(FileSetDescriptor fileSetDescriptor) {
        try {
            return dspaceClient.retrieveFileSet(fileSetDescriptor);
        } catch (DSpaceClientException e) {
            throw new FileManagerException(e.getMessage(), e);
        }
    }

    @Override
    public void changePermissions(FileSetDescriptor fileSetDescriptor, Boolean privateFileSet) {
        try {
            dspaceClient.changeFileSetPermissions(fileSetDescriptor, privateFileSet.booleanValue());
        } catch (DSpaceClientException e) {
            throw new FileManagerException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteFileSet(FileSetDescriptor fileSetDescriptor) {
        try {
            dspaceClient.deleteFileSet(fileSetDescriptor);
        } catch (DSpaceClientException e) {
            throw new FileManagerException(e.getMessage(), e);
        }
    }

    public boolean isDirectDownloadURISupported() {
        return true;
    }

    @Override
    public FileSetDescriptor listAllDescriptorsFromRoot(FileSetDescriptor rootFileSetDescriptor) {
        try {
            return dspaceClient.listAllDescriptorsFromRoot(rootFileSetDescriptor);
        } catch (DSpaceClientException e) {
            throw new FileManagerException(e.getMessage(), e);
        }
    }

    @Override
    public FileSetDescriptor getRootDescriptor(FileSetDescriptor innerChildDescriptor) {

        try {
            return dspaceClient.getRootDescriptor(innerChildDescriptor);
        } catch (DSpaceClientException e) {
            throw new FileManagerException(e.getMessage(), e);
        }
    }

    public FileSetQueryResults searchFileSets(FilesetMetadataQuery query, VirtualPath optionalPathToRestrictSearch) {
        try {
            return dspaceClient.searchFileSets(query, optionalPathToRestrictSearch);
        } catch (DSpaceClientException e) {
            throw new FileManagerException(e.getMessage(), e);
        }
    }

    @Override
    public FileSearchResult searchFiles(FileSearchCriteria criteria) {
        try {
            return dspaceClient.searchFiles(criteria);
        } catch (DSpaceClientException e) {
            throw new FileManagerException(e.getMessage(), e);
        }
    }

    @Override
    public FileSearchResult searchFiles(FileSearchCriteria criteria, VirtualPath optionalPathToRestrictSearch) {
        try {
            return dspaceClient.searchFiles(criteria, optionalPathToRestrictSearch);
        } catch (DSpaceClientException e) {
            throw new FileManagerException(e.getMessage(), e);
        }
    }

}
