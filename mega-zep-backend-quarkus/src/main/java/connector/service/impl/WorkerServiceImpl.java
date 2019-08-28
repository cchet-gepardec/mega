package connector.service.impl;

import connector.rest.model.GoogleUser;
import connector.service.api.WorkerService;
import de.provantis.zep.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class WorkerServiceImpl implements WorkerService {

    @Inject
    ZepSoapPortType zepSoapPortType;

    @Inject
    RequestHeaderType requestHeaderType;

    @Override
    public ReadMitarbeiterResponseType getAll(GoogleUser user) {
        ReadMitarbeiterRequestType empl = new ReadMitarbeiterRequestType();
        empl.setRequestHeader(requestHeaderType);

        return zepSoapPortType.readMitarbeiter(empl);

    }

    @Override
    public MitarbeiterType get(GoogleUser user) {
        ReadMitarbeiterRequestType empl = new ReadMitarbeiterRequestType();
        empl.setRequestHeader(requestHeaderType);

        ReadMitarbeiterResponseType rmrt = zepSoapPortType.readMitarbeiter(empl);
        return rmrt.getMitarbeiterListe().getMitarbeiter().stream()
                .filter(emp -> user.getEmail().equals(emp.getEmail()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Response updateWorker(List<MitarbeiterType> employees) {
        UpdateMitarbeiterRequestType umrt = new UpdateMitarbeiterRequestType();
        umrt.setRequestHeader(requestHeaderType);


        for(MitarbeiterType mt : employees){
            umrt.setMitarbeiter(mt);
            UpdateMitarbeiterResponseType umrest = zepSoapPortType.updateMitarbeiter(umrt);
        }
        return Response.ok().build();

    }
}
