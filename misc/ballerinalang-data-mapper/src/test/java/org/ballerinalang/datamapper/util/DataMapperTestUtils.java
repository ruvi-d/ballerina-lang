package org.ballerinalang.datamapper.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.ballerinalang.langserver.codeaction.CodeActionUtil;
import org.ballerinalang.langserver.common.utils.CommonUtil;
import org.ballerinalang.langserver.commons.workspace.WorkspaceManager;
import org.ballerinalang.langserver.workspace.BallerinaWorkspaceManager;
import org.eclipse.lsp4j.CodeActionContext;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.jsonrpc.Endpoint;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Common utils that are reused within data-mapper test suits.
 */
public class DataMapperTestUtils {

    private static JsonParser parser = new JsonParser();

    private static Path sourcesPath = new File(DataMapperTestUtils.class.getClassLoader().getResource("codeaction")
            .getFile()).toPath();

    private static final WorkspaceManager workspaceManager = new BallerinaWorkspaceManager();

    private static JsonObject getResponseJson(String response) {
        JsonObject responseJson = parser.parse(response).getAsJsonObject();
        responseJson.remove("id");
        return responseJson;
    }

    public static JsonObject getCodeActionResponse(String source, JsonObject configJsonObject, Endpoint serviceEndpoint)
            throws IOException {

        // Read expected results
        Path sourcePath = sourcesPath.resolve("source").resolve(source);
        TestUtil.openDocument(serviceEndpoint, sourcePath);

        // Filter diagnostics for the cursor position
        List<Diagnostic> diags = new ArrayList<>(
                CodeActionUtil.toDiagnostics(TestUtil.compileAndGetDiagnostics(sourcePath, workspaceManager)));
        Position pos = new Position(configJsonObject.get("line").getAsInt(),
                configJsonObject.get("character").getAsInt());
        diags = diags.stream().
                filter(diag -> CommonUtil.isWithinRange(pos, diag.getRange()))
                .collect(Collectors.toList());
        CodeActionContext codeActionContext = new CodeActionContext(diags);
        Range range = new Range(pos, pos);
        String response = TestUtil.getCodeActionResponse(serviceEndpoint, sourcePath.toString(), range,
                codeActionContext);
        TestUtil.closeDocument(serviceEndpoint, sourcePath);

        return getResponseJson(response);
    }
}
