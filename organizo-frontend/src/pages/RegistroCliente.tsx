import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Button } from '../components/Button';
import { toast } from 'react-hot-toast';
import api from '../services/api';

/**
 * Página de Registro de Cliente
 * - Formulário para cadastro de novos clientes
 * - Validação de campos
 * - Envio para API
 * - Redirecionamento para login após sucesso
 */
export default function RegistroCliente() {
  const navigate = useNavigate();

  // Estados para os campos do formulário
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [confirmarSenha, setConfirmarSenha] = useState('');
  const [telefone, setTelefone] = useState('');

  // Estado para controlar o loading durante o envio
  const [loading, setLoading] = useState(false);

  // Estado para mensagens de erro
  const [erro, setErro] = useState<string | null>(null);

  // Função para validar o formulário
  const validarFormulario = () => {
    // Resetar erro anterior
    setErro(null);

    // Validar campos obrigatórios
    if (!nome || !email || !senha || !confirmarSenha) {
      setErro('Todos os campos são obrigatórios.');
      return false;
    }

    // Validar formato de email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      setErro('Por favor, insira um email válido.');
      return false;
    }

    // Validar senha (mínimo 6 caracteres)
    if (senha.length < 6) {
      setErro('A senha deve ter pelo menos 6 caracteres.');
      return false;
    }

    // Validar confirmação de senha
    if (senha !== confirmarSenha) {
      setErro('As senhas não coincidem.');
      return false;
    }

    return true;
  };

  // Função para lidar com o envio do formulário
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // Validar formulário
    if (!validarFormulario()) {
      return;
    }

    // Iniciar loading
    setLoading(true);

    try {
      // Preparar dados para envio
      const dadosRegistro = {
        nome,
        email,
        senha,
        telefone: telefone || undefined // Enviar apenas se preenchido
      };

      // Enviar requisição para a API - ROTA CORRIGIDA
      await api.post('/api/auth/register', dadosRegistro);

      // Mostrar mensagem de sucesso
      toast.success('Registro realizado com sucesso! Faça login para continuar.');

      // Redirecionar para a página de login
      navigate('/login');
    } catch (error: any) {
      // Tratar erro da API
      const mensagemErro = error.response?.data?.message || 'Erro ao realizar o registro. Tente novamente.';
      setErro(mensagemErro);
      toast.error('Falha no registro.');
      console.error('Erro no registro:', error);
    } finally {
      // Finalizar loading
      setLoading(false);
    }
  };

  return (
    <main className="container flex flex-col items-center justify-center min-h-screen py-lg">
      <div className="w-full max-w-md p-lg bg-background-card rounded-lg shadow-md">
        <h1 className="text-2xl font-bold text-center mb-lg">Criar Conta</h1>

        {/* Formulário de Registro */}
        <form onSubmit={handleSubmit} className="space-y-md">
          {/* Exibição de erro */}
          {erro && (
            <div className="p-sm bg-red-100 border border-red-300 text-red-700 rounded text-sm">
              {erro}
            </div>
          )}

          {/* Campo Nome */}
          <div>
            <label htmlFor="nome" className="block text-sm font-medium text-text mb-xs">
              Nome Completo
            </label>
            <input
              id="nome"
              type="text"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              className="w-full p-sm border border-border rounded-md focus:outline-none focus:ring-2 focus:ring-accent"
              required
              placeholder="Seu nome completo"
            />
          </div>

          {/* Campo Email */}
          <div>
            <label htmlFor="email" className="block text-sm font-medium text-text mb-xs">
              E-mail
            </label>
            <input
              id="email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full p-sm border border-border rounded-md focus:outline-none focus:ring-2 focus:ring-accent"
              required
              placeholder="seu@exemplo.com"
            />
          </div>

          {/* Campo Senha */}
          <div>
            <label htmlFor="senha" className="block text-sm font-medium text-text mb-xs">
              Senha
            </label>
            <input
              id="senha"
              type="password"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              className="w-full p-sm border border-border rounded-md focus:outline-none focus:ring-2 focus:ring-accent"
              required
              minLength={6}
              placeholder="Mínimo 6 caracteres"
            />
          </div>

          {/* Campo Confirmar Senha */}
          <div>
            <label htmlFor="confirmarSenha" className="block text-sm font-medium text-text mb-xs">
              Confirmar Senha
            </label>
            <input
              id="confirmarSenha"
              type="password"
              value={confirmarSenha}
              onChange={(e) => setConfirmarSenha(e.target.value)}
              className="w-full p-sm border border-border rounded-md focus:outline-none focus:ring-2 focus:ring-accent"
              required
              minLength={6}
              placeholder="Digite a senha novamente"
            />
          </div>

          {/* Campo Telefone (opcional) */}
          <div>
            <label htmlFor="telefone" className="block text-sm font-medium text-text mb-xs">
              Telefone (opcional)
            </label>
            <input
              id="telefone"
              type="tel"
              value={telefone}
              onChange={(e) => setTelefone(e.target.value)}
              className="w-full p-sm border border-border rounded-md focus:outline-none focus:ring-2 focus:ring-accent"
              placeholder="(00) 00000-0000"
            />
          </div>

          {/* Botão de Submit */}
          <Button type="submit" variant="primary" className="w-full" disabled={loading}>
            {loading ? 'Registrando...' : 'Criar Conta'}
          </Button>
        </form>

        {/* Link para Login */}
        <p className="text-center text-sm text-muted mt-lg">
          Já tem uma conta?{' '}
          <Link to="/login" className="font-medium text-accent hover:text-accent-hover">
            Faça login
          </Link>
        </p>
      </div>
    </main>
  );
}
