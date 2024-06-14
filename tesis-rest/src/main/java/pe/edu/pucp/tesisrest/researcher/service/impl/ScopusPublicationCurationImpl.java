package pe.edu.pucp.tesisrest.researcher.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.pucp.tesisrest.common.model.funding.Funding;
import pe.edu.pucp.tesisrest.common.model.orgunit.Funder;
import pe.edu.pucp.tesisrest.common.model.orgunit.FunderId;
import pe.edu.pucp.tesisrest.common.model.project.*;
import pe.edu.pucp.tesisrest.common.model.publication.*;
import pe.edu.pucp.tesisrest.common.model.scopus.ScopusAuthor;
import pe.edu.pucp.tesisrest.common.model.scopus.ScopusPublication;
import pe.edu.pucp.tesisrest.common.repository.PublicationAuthorRepository;
import pe.edu.pucp.tesisrest.common.repository.PublicationRepository;
import pe.edu.pucp.tesisrest.common.util.DateConversionUtil;
import pe.edu.pucp.tesisrest.common.util.ValidationUtils;
import pe.edu.pucp.tesisrest.researcher.dto.NewFundingDto;
import pe.edu.pucp.tesisrest.researcher.dto.NewProjectDto;
import pe.edu.pucp.tesisrest.researcher.dto.ProjectTeamPUCPDto;
import pe.edu.pucp.tesisrest.researcher.dto.request.NewProjectWithFundingRequest;
import pe.edu.pucp.tesisrest.researcher.dto.request.NewPublicationOfSPCurationRequest;
import pe.edu.pucp.tesisrest.researcher.dto.response.NewProjectWithFundingResponse;
import pe.edu.pucp.tesisrest.researcher.dto.response.NewPublicationOfSPCurationResponse;
import pe.edu.pucp.tesisrest.researcher.repository.*;
import pe.edu.pucp.tesisrest.researcher.repository.scopus.ScopusAuthorPublicationRepository;
import pe.edu.pucp.tesisrest.researcher.repository.scopus.ScopusAuthorRepository;
import pe.edu.pucp.tesisrest.researcher.repository.scopus.ScopusPublicationRepository;
import pe.edu.pucp.tesisrest.researcher.service.ScopusPublicationCurationService;

import java.sql.Date;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScopusPublicationCurationImpl implements ScopusPublicationCurationService {

    private final ValidationUtils validationUtils;

    private final FunderRepository funderRepository;
    private final FundingRepository fundingRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTeamPUCPRepository projectTeamPUCPRepository;

    private final ProjectFundedRepository projectFundedRepository;
    private final PublicationRepository publicationRepository;
    private final ScopusPublicationRepository scopusPublicationRepository;
    private final PublicationOriginatesFromRepository publicationOriginatesFromRepository;
    private final ScopusAuthorPublicationRepository scopusAuthorPublicationRepository;
    private final ScopusAuthorRepository scopusAuthorRepository;
    private final PublicationAuthorRepository publicationAuthorRepository;

    @Override
    @Transactional
    public NewProjectWithFundingResponse createNewProjectWithFunding(NewProjectWithFundingRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        NewProjectWithFundingResponse response = new NewProjectWithFundingResponse();

        try {
            // Obtener el último idProject
            String lastIdProject = projectRepository.findLastIdProject();
            lastIdProject = (lastIdProject == null) ? "1" : lastIdProject;
            int lastNumericId = Integer.parseInt(lastIdProject);

            // Incrementar la parte numérica para el nuevo idProject
            String newIdProject = String.valueOf((lastNumericId + 1));

            Project project = new Project();
            project.setIdProject(newIdProject);
            project.setTitle(request.getTitle());
            project.setStartDate(Date.valueOf(DateConversionUtil.convertToLocalDate(request.getStartDate())));
            project.setEndDate(Date.valueOf(DateConversionUtil.convertToLocalDate(request.getEndDate())));
            project.setIdProjectStatusTypeCONCYTEC(request.getIdProjectStatusTypeConcytec());
            project.setIdentifier("INTERNO-" + newIdProject);
            project.setLangCode("es");

            project.setAbstractProject(null);
            project.setAcronym(null);
            project.setResume(null);
            project.setUrl(null);

            projectRepository.save(project);

            // Save project team - project_team_pucp
            saveTeamProject(request, newIdProject);

            // Save related funding -- 1.funding 2.funder 3.project_funded
            for (NewFundingDto item : request.getFunding()) {
                if(item.getIdFunding() == null){
                    createNewFundingV1(item, newIdProject);
                }
                else{
                    updateRelatedFunding(item, newIdProject);
                }
            }

            //createNewFundingV1(request, newIdProject);

            NewProjectDto newProjectDto = new NewProjectDto();
            newProjectDto.setProject(project);

            response.setNewProjectDto(newProjectDto);
        } catch (Exception e) {
            throw e;
        }

        return response;
    }

    @Override
    public NewPublicationOfSPCurationResponse createNewPublicationOfSPCuration(NewPublicationOfSPCurationRequest request) {
        validationUtils.validateKeyCode(request.getKeyCode());
        NewPublicationOfSPCurationResponse response = new NewPublicationOfSPCurationResponse();

        Optional<ScopusPublication> scopusPublication = scopusPublicationRepository.findScopusPublicationByScopusPublicationId(Long.valueOf(request.getScopusPublicationId()));

        if (scopusPublication.isPresent()) {
            Publication publicationExist = publicationRepository.findPublicationByDOI(scopusPublication.get().getDoi());
            if (publicationExist != null) {
                System.out.println("YA EXISTE");

                response.setPublication(publicationExist);

                Optional<ScopusAuthor> scopusAuthor = scopusAuthorRepository.findByAuthid(request.getScopusAuthorId());
                if (scopusAuthor.isPresent()) {
                    scopusAuthorPublicationRepository.updateStatusByScopusAuthorId(0, scopusAuthor.get().getScopusAuthorId(), request.getScopusPublicationId());
                    System.out.println("UPDATEO EL STATUS");

                    // Se registra en publication_author los autores
                    relatePublicationAuthor(request, publicationExist.getIdPublication(), scopusAuthor);
                    System.out.println("TERMINA RELACION PUBLICATION AUTHOR");
                }
            }
            else {
                System.out.println("NUEVA");
                // Obtener el último idPublication
                Integer lastIdPublication = publicationRepository.findLastIdPublication();
                int newIdPublication = (lastIdPublication == null) ? 1 : lastIdPublication + 1;

                System.out.println(newIdPublication);

                Publication publication = new Publication();
                publication.setIdPublication(newIdPublication);
                publication.setTitle(scopusPublication.get().getTitle());
                publication.setDOI(scopusPublication.get().getDoi());
                publication.setSCPNumber(scopusPublication.get().getEid());
                publication.setPublishedInDesc(scopusPublication.get().getPublicationName());
                publication.setPublicationDateDesc(scopusPublication.get().getCoverDate());
                publication.setPublicationDate(Date.valueOf(DateConversionUtil.convertToLocalDate(scopusPublication.get().getCoverDate())));
                publication.setVolume(scopusPublication.get().getVolume());

                String coverDate = scopusPublication.get().getCoverDate();
                String[] numbersDate = coverDate.split("-");
                publication.setPublicationYear(Integer.parseInt(numbersDate[0]));

                String pageRange = scopusPublication.get().getPageRange();
                String[] pages = pageRange.split("-");
                publication.setStartPage(pages[0]);
                publication.setEndPage(pages[1]);

                publication.setAbstractText(scopusPublication.get().getDescription());

                String IdResourceTypeCOA = conversionSubTypeDescriptionToIdSourceTypeCOAR(scopusPublication.get().getSubTypeDescription(), scopusPublication.get().getAggregationType());
                publication.setIdResourceTypeCOAR(IdResourceTypeCOA);

                publication.setEdition(null);
                publication.setHandle(null);
                publication.setIdAccessRightCOAR(null);
                publication.setIdAcademicDegreePUCP(null);
                publication.setIdResearchTypeRENATI(null);
                publication.setIdLicenseType(null);
                publication.setPMCID(null);
                publication.setISINumber(null);
                publication.setPublishedInCode(null);
                publication.setURL(null);
                publication.setPartOf(null);
                publication.setNumber(null);
                publication.setIssue(null);
                publication.setLangCode(null);

                publicationRepository.save(publication);

                // Vincular proyectos y publicacion
                for (String item : request.getProjectIds()) {
                    PublicationOriginatesFromId publicationOriginatesFromId = new PublicationOriginatesFromId(newIdPublication, item);
                    PublicationOriginatesFrom publicationOriginatesFrom = new PublicationOriginatesFrom(publicationOriginatesFromId);
                    publicationOriginatesFromRepository.save(publicationOriginatesFrom);
                }

                // Se actualiza el estado = 0 en scopus_author_publication para indicar que ese autor ya realizó la curación
                Optional<ScopusAuthor> scopusAuthor = scopusAuthorRepository.findByAuthid(request.getScopusAuthorId());
                if (scopusAuthor.isPresent()) {
                    System.out.println("scopus author id" + scopusAuthor.get().getScopusAuthorId());
                    scopusAuthorPublicationRepository.updateStatusByScopusAuthorId(0, scopusAuthor.get().getScopusAuthorId(), request.getScopusPublicationId());
                    System.out.println("UPDATEO EL STATUS");

                    // Se registra en publication_author los autores
                    relatePublicationAuthor(request, newIdPublication, scopusAuthor);
                    System.out.println("TERMINA RELACION PUBLICATION AUTHOR");
                }

                response.setPublication(publication);
            }
        }

        return response;
    }

    private void relatePublicationAuthor(NewPublicationOfSPCurationRequest request, int newIdPublication, Optional<ScopusAuthor> scopusAuthor) {
        System.out.println("INICIA RELACION PUBLICATION AUTHOR");

        PublicationAuthorId publicationAuthorId = new PublicationAuthorId(newIdPublication, 1);
        PublicationAuthor publicationAuthor = new PublicationAuthor();

        publicationAuthor.setId(publicationAuthorId);
        publicationAuthor.setScopusAuthorID(String.valueOf(request.getScopusAuthorId()));
        publicationAuthor.setIdPerson(request.getIdPerson());

        publicationAuthor.setIdOrgUnit(request.getResearchGroupIdOrg()); // Con el grupo seleccionado

        publicationAuthor.setLinkPersonDate(null);
        publicationAuthor.setUpdatePerson(null);

        publicationAuthor.setDisplayName(scopusAuthor.get().getAuthorName());
        publicationAuthor.setSurname(scopusAuthor.get().getSurname());
        publicationAuthor.setGivenName(scopusAuthor.get().getGivenName());

        publicationAuthorRepository.save(publicationAuthor);
    }

    private String conversionSubTypeDescriptionToIdSourceTypeCOAR(String subTypeDescription, String aggregationType) {

        switch (subTypeDescription) {
            case "Article" -> {
                switch (aggregationType) {
                    case "Journal" -> {
                        return "ARTICULO_INVESTIGACION";
                    }
                }
                return "ARTICULO_REVISTA";
            }
            case "Book" -> {
                return "LIBRO";
            }
            case "Book Chapter" -> {
                return "CAPITULO_LIBRO";
            }
            case "Conference Paper" -> {
                return "COMUNICACION_CONGRESO";
            }
            case "Data Paper" -> {
                return "ARTICULO_DATOS";
            }
            case "Editorial" -> {
                return "EDITORIAL";
            }
            case "Letter" -> {
                return "CARTA";
            }
            case "Review" -> {
                switch (aggregationType) {
                    case "Journal" -> {
                        return "ARTICULO_REVISION";
                    }
                    case "Book Series" -> {
                        return "RESEÑA_LIBRE";
                    }
                }
                return "RESEÑA";
            }
        }

        return null;
    }

    private void createNewFundingV1(NewFundingDto item, String idProject) {
        // Obtener el último idFunding
        String lastIdFunding = fundingRepository.findLastIdFunding();
        lastIdFunding = (lastIdFunding == null) ? "1" : lastIdFunding;
        int lastNumericId = Integer.parseInt(lastIdFunding);

        // Incrementar la parte numérica para el nuevo idFunding
        String newIdFunding = String.valueOf((lastNumericId + 1));

        Funding funding = new Funding();
        funding.setIdFunding(newIdFunding);
        funding.setName(item.getFundedAs() + " " + item.getCategory());
        funding.setIdentifier(item.getFundingType() + "-" + idProject);
        funding.setCurrCode(item.getCurrCode());
        funding.setAmount(item.getAmount());
        funding.setIdFundingType(item.getFundingType());

        // Guardar todos los campos restantes en null
        funding.setAcronym(null);
        funding.setPartOf(null);
        funding.setStartDate(null);
        funding.setEndDate(null);
        funding.setExecutedAmount(null);
        funding.setDescription(null);

        fundingRepository.save(funding);

        FunderId funderId = new FunderId(item.getFundedBy(), funding.getIdFunding());
        Funder funder = new Funder(funderId);
        funderRepository.save(funder);

        ProjectFundedId projectFundedId = new ProjectFundedId(idProject, funding.getIdFunding());

        ProjectFunded projectFunded = new ProjectFunded();
        projectFunded.setId(projectFundedId);
        projectFunded.setFundedAs(item.getFundedAs());
        projectFunded.setCategoria(item.getCategory());

        // Validar datos a registrar
        projectFunded.setCodigoPropuesta(null);
        projectFunded.setCodigoOGP(null);

        projectFundedRepository.save(projectFunded);

        /*
        // Verificar si la lista de funding está vacía
        if (request.getFunding() == null || request.getFunding().isEmpty()) {
            return;
        }

        // Obtener el último idFunding
        String lastIdFunding = fundingRepository.findLastIdFunding();
        lastIdFunding = (lastIdFunding == null) ? "1" : lastIdFunding;
        int lastNumericId = Integer.parseInt(lastIdFunding);

        // Incrementar la parte numérica para el nuevo idFunding
        String newIdFunding = String.valueOf((lastNumericId + 1));

        for (NewFundingDto item : request.getFunding()) {
            Funding funding = new Funding();
            funding.setIdFunding(newIdFunding);
            funding.setName(item.getFundedAs() + " " + item.getCategory());
            funding.setIdentifier(item.getFundingType() + "-" + idProject);
            funding.setCurrCode(item.getCurrCode());
            funding.setAmount(item.getAmount());
            funding.setIdFundingType(item.getFundingType());

            // Guardar todos los campos restantes en null
            funding.setAcronym(null);
            funding.setPartOf(null);
            funding.setStartDate(null);
            funding.setEndDate(null);
            funding.setExecutedAmount(null);
            funding.setDescription(null);

            fundingRepository.save(funding);

            FunderId funderId = new FunderId(item.getFundedBy(), funding.getIdFunding());
            Funder funder = new Funder(funderId);
            funderRepository.save(funder);

            ProjectFundedId projectFundedId = new ProjectFundedId(idProject, funding.getIdFunding());

            ProjectFunded projectFunded = new ProjectFunded();
            projectFunded.setId(projectFundedId);
            projectFunded.setFundedAs(item.getFundedAs());
            projectFunded.setCategoria(item.getCategory());

            // Validar datos a registrar
            projectFunded.setCodigoPropuesta(null);
            projectFunded.setCodigoOGP(null);

            projectFundedRepository.save(projectFunded);
        }
        */
    }

    private void saveTeamProject(NewProjectWithFundingRequest request, String idProject) {
        // Verificar si la lista de funding está vacía
        if (request.getProjectTeam() == null || request.getProjectTeam().isEmpty()) {
            return;
        }

        // Obtener el último last Seq relacionado al idProject
        Integer lastSeq = projectTeamPUCPRepository.findLastSeq(idProject);
        int newSeq = (lastSeq == null) ? 1 : lastSeq + 1;

        for (ProjectTeamPUCPDto item : request.getProjectTeam()) {
            ProjectTeamPUCPId projectTeamPUCPId = new ProjectTeamPUCPId();
            projectTeamPUCPId.setIdProject(idProject);
            projectTeamPUCPId.setSeqMember(newSeq);

            ProjectTeamPUCP team = new ProjectTeamPUCP();
            team.setId(projectTeamPUCPId);
            team.setIdPerson(item.getIdPerson());
            team.setIdPersonRole(item.getIdPersonRole());

            team.setTipoRecurso(null);
            team.setIdOrgUnit(null);
            team.setName(null);

            projectTeamPUCPRepository.save(team);

            newSeq++;
        }
    }

    private void updateRelatedFunding(NewFundingDto item, String idProject) {

        ProjectFundedId projectFundedId = new ProjectFundedId(idProject, item.getIdFunding());

        ProjectFunded projectFunded = new ProjectFunded();
        projectFunded.setId(projectFundedId);
        projectFunded.setFundedAs(item.getFundedAs());
        projectFunded.setCategoria(item.getCategory());

        // Validar datos a registrar
        projectFunded.setCodigoPropuesta(null);
        projectFunded.setCodigoOGP(null);

        projectFundedRepository.save(projectFunded);
    }
}
