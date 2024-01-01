package com.commigo.metaclass.MetaClass.gestioneamministrazione.service;

import com.commigo.metaclass.MetaClass.entity.Categoria;
import com.commigo.metaclass.MetaClass.entity.Scenario;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.CategoriaRepository;
import com.commigo.metaclass.MetaClass.gestioneamministrazione.repository.ScenarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("GestioneAmministrazioneService")
public class GestioneAmministrazioneServiceImpl implements GestioneAmministrazioneService{

    @Autowired
    @Qualifier("CategoriaRepository")
    private CategoriaRepository categoriaRepository;

    @Autowired
    @Qualifier("ScenarioRepository")
    private ScenarioRepository scenarioRepository;

    /**
     * @param c
     * @return
     */
    @Override
    public boolean updateCategoria(Categoria c) {
         Categoria cat;
         if((cat = categoriaRepository.findByNome(c.getNome()))==null){
             categoriaRepository.save(c);
             return true;
         }
         return false;
    }

    /**
     * @param s
     * @return
     */
    @Override
    public boolean updateScenario(Scenario s, long IdCategoria) {

        //gestione della categoria
        Categoria cat;
        if((cat = categoriaRepository.findById(IdCategoria))==null)   return false;
        s.setCategoria(cat);

        //getsione dello scenario
        if((scenarioRepository.findByNome(s.getNome()))==null){
            scenarioRepository.save(s);
            return true;
        }
        return false;
    }
}
