package ma.irirsi.gestionpaieservice.domain.process.echelon;

import ma.irirsi.gestionpaieservice.domain.core.AbstractProcessImpl;
import ma.irirsi.gestionpaieservice.domain.core.Result;
import ma.irirsi.gestionpaieservice.domain.pojo.Echelon;
import ma.irirsi.gestionpaieservice.infra.facade.EchelleInfra;
import ma.irirsi.gestionpaieservice.infra.facade.EchelonInfra;

import java.math.BigDecimal;

public class EchelonCreateProcessImpl extends AbstractProcessImpl<EchelonCreateProcessInput> implements EchelonCreateProcess {

    EchelonInfra echelonInfra;
    EchelleInfra echelleInfra;

    public EchelonCreateProcessImpl(EchelonInfra echelonInfra, EchelleInfra echelleInfra) {
        this.echelonInfra = echelonInfra;
        this.echelleInfra = echelleInfra;
    }

    @Override
    public void validate(EchelonCreateProcessInput echelonCreateProcessInput, Result result) {
        Echelon echelon = echelonCreateProcessInput.getEchelon();

        if (echelon == null) {
            result.addErrorMessage(echelonInfra.getMessage("echelon.create.error.echelon.null"));
        } else {
            if (echelon.getId() != null) {
                result.addErrorMessage(echelonInfra.getMessage("echelon.create.error.id.not.null"));
            }

            if (echelon.getRef() == null) {
                result.addErrorMessage(echelonInfra.getMessage("echelon.create.error.ref.null"));
            } else {
                if (echelonInfra.findByRef(echelon.getRef()) != null) {
                    result.addErrorMessage(echelonInfra.getMessage("echelon.create.error.ref.already.exist"));
                }
            }

            if (echelon.getLibelle() == null) {
                result.addErrorMessage(echelonInfra.getMessage("echelon.create.error.libelle.null"));
            }

            if (echelon.getNiveau() == null) {
                result.addErrorMessage(echelonInfra.getMessage("echelon.create.error.niveau.null"));
            } else {
                if (echelon.getNiveau() < 1) {
                    result.addErrorMessage(echelonInfra.getMessage("echelon.create.error.niveau.invalid"));
                }
            }

            if (echelon.getMontant() == null) {
                result.addErrorMessage(echelonInfra.getMessage("echelon.create.error.montant.null"));
            } else {
                if (echelon.getMontant().compareTo(BigDecimal.ZERO) < 0) {
                    result.addErrorMessage(echelonInfra.getMessage("echelon.create.error.montant.invalid"));
                }
            }

            if (echelon.getEchelle() == null) {
                result.addErrorMessage(echelonInfra.getMessage("echelon.create.error.echelle.null"));
            } else {
                if (echelon.getEchelle().getId() == null) {
                    result.addErrorMessage(echelonInfra.getMessage("echelon.create.error.echelle.id.null"));
                } else {
                    if (echelleInfra.findById(echelon.getEchelle().getId()) == null) {
                        result.addErrorMessage(echelonInfra.getMessage("echelon.create.error.echelle.not.exist"));
                    } else {
                        if (echelonInfra.findEchelonByNiveauAndEchelleId(echelon.getNiveau(), echelon.getEchelle().getId()) != null) {
                            result.addErrorMessage(echelonInfra.getMessage("echelon.create.error.niveau.already.exist.for.echelle"));
                        } else {
                            Echelon previousEchelon = echelonInfra.findEchelonByNiveauAndEchelleId(echelon.getNiveau(), echelon.getEchelle().getId());
                            if (previousEchelon != null && previousEchelon.getMontant() != null && echelon.getMontant() != null) {
                                if (previousEchelon.getMontant().compareTo(echelon.getMontant()) >= 0) {
                                    result.addErrorMessage(echelonInfra.getMessage("echelon.create.error.montant.should.be.greater.than.previous.echelon"));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void run(EchelonCreateProcessInput echelonCreateProcessInput, Result result) {
        Echelon echelon = echelonInfra.save(echelonCreateProcessInput.getEchelon());
        result.setOutput(echelon);
        result.addInfoMessage(echelonInfra.getMessage("echelon.create.success"));
    }
}
