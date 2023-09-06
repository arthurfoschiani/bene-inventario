package br.com.fiap;

import br.com.fiap.domain.entity.Bem;
import br.com.fiap.domain.entity.Departamento;
import br.com.fiap.domain.entity.Inventario;
import br.com.fiap.domain.entity.TipoDeBem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("oracle");
        EntityManager manager = factory.createEntityManager();

        //TipoDeBem tipoDeBem = addTipoBem(manager);
        //System.out.println(tipoDeBem);

        //Departamento departamento = addDepartamento(manager);
        //System.out.println(departamento);

        //Bem bem = addBem(manager);
        //System.out.println(bem);

        //Inventario inventario = addInventario(manager);
        //System.out.println(inventario);

        //listarTodosBens(manager);

        Inventario inventario = buscarinvertarioPorId(manager);
        System.out.println(inventario);

        List<Bem> bens = findAllBens(manager);

        for (Bem b: bens) {
            inventario.addBem(b);
        }

        manager.getTransaction().begin();
        manager.persist(inventario);
        manager.getTransaction().commit();

        manager.close();
        factory.close();
    }

    private static List<Bem> findAllBens(EntityManager manager) {
        String jpql = "FROM Bem";
        return manager.createQuery(jpql).getResultList();
    }

    private static Inventario addInventario(EntityManager manager) {
        boolean salvou = false;

        do {
            String relatorio = JOptionPane.showInputDialog("Relatório: ");

            String inicio = JOptionPane.showInputDialog("Data de início no formato DD/MM/AAAA");
            int dia = Integer.parseInt(inicio.substring(0,2));
            int mes = Integer.parseInt(inicio.substring(3,5));
            int ano = Integer.parseInt(inicio.substring(6,10));

            List<Departamento> departamentos = manager.createQuery("FROM Departamento").getResultList();
            Departamento departamentoSelecionado = (Departamento) JOptionPane.showInputDialog(null, "Selecione o departamento", "Selecione uma opção", JOptionPane.QUESTION_MESSAGE, null, departamentos.toArray(), departamentos.get(0));

            Inventario inventario = new Inventario().setRelatorio(relatorio).setInicio(LocalDate.of(ano, mes, dia)).setDepartamento(departamentoSelecionado);

            try {
                manager.getTransaction().begin();
                manager.persist(inventario);
                manager.getTransaction().commit();
                salvou = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: \nNão foi possível salvar o Advogado. " + e.getMessage());
            }
            return inventario;
        } while (salvou == false);
    }

    private static Bem addBem(EntityManager manager) {
        boolean salvou = false;

        do {
            String nome = JOptionPane.showInputDialog("Nome: ");

            String etiqueta = JOptionPane.showInputDialog("Etiqueta: ");

            List<TipoDeBem> tipoDeBem = manager.createQuery("FROM TipoDeBem").getResultList();
            TipoDeBem tipoBemSelecionado = (TipoDeBem) JOptionPane.showInputDialog(null, "Selecione o tipo de bem", "Selecione uma opção", JOptionPane.QUESTION_MESSAGE, null, tipoDeBem.toArray(), tipoDeBem.get(0));

            List<Departamento> departamentos = manager.createQuery("FROM Departamento").getResultList();
            Departamento departamentoSelecionado = (Departamento) JOptionPane.showInputDialog(null, "Selecione o departamento", "Selecione uma opção", JOptionPane.QUESTION_MESSAGE, null, departamentos.toArray(), departamentos.get(0));

            String aquisicao = JOptionPane.showInputDialog("Data de aquisição no formato DD/MM/AAAA");
            int dia = Integer.parseInt(aquisicao.substring(0,2));
            int mes = Integer.parseInt(aquisicao.substring(3,5));
            int ano = Integer.parseInt(aquisicao.substring(6,10));

            Bem bem = new Bem().setNome(nome).setEtiqueta(etiqueta).setAquisicao(LocalDate.of(ano, mes, dia)).setTipo(tipoBemSelecionado).setLocalizacao(departamentoSelecionado);

            try {
                manager.getTransaction().begin();
                manager.persist(bem);
                manager.getTransaction().commit();
                salvou = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: \nNão foi possível salvar o Advogado. " + e.getMessage());
            }
            return bem;
        } while (salvou == false);
    }

    private static Departamento addDepartamento(EntityManager manager) {
        boolean salvou = false;

        do {
            String nome = JOptionPane.showInputDialog("Nome departamento: ");

            Departamento departamento = new Departamento().setNome(nome);

            try {
                manager.getTransaction().begin();
                manager.persist(departamento);
                manager.getTransaction().commit();
                salvou = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: \nNão foi possível salvar o departamento. " + e.getMessage());
            }
            return departamento;
        } while (salvou == false);
    }

    private static TipoDeBem addTipoBem(EntityManager manager) {
        boolean salvou = false;

        do {
            String nome = JOptionPane.showInputDialog("Tipo de bem: ");

            TipoDeBem tipo = new TipoDeBem().setNome(nome);

            try {
                manager.getTransaction().begin();
                manager.persist(tipo);
                manager.getTransaction().commit();
                salvou = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: \nNão foi possível salvar o tipo de bem. " + e.getMessage());
            }
            return tipo;
        } while (salvou == false);
    }

    private static void listarTodosBens(EntityManager manager) {
        List<Bem> bens = manager.createQuery("FROM Bem").getResultList();
        for (Bem bem: bens) {
            System.out.println(bem);
        }
    }

    private static Inventario buscarinvertarioPorId(EntityManager manager) {
        String id = JOptionPane.showInputDialog("Digite o id do inventário que deseja buscar: ");
        Inventario inventario = manager.find(Inventario.class, id);
        return inventario;
    }
}