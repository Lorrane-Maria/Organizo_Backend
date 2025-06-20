import React, { useState, FormEvent, useContext } from 'react';
import { Link } from 'react-router-dom'; // Importar o Link para navegação
import { AuthContext } from '../contexts/AuthContext';
import { Button } from '../components/Button';
import { Input } from '../components/Input'; // Usar o componente Input estilizado

/**
 * Página de login:
 * - Solicita e-mail e senha.
 * - Chama AuthContext.login() para autenticar.
 * - Exibe mensagens de erro.
 * - Oferece link para a página de registro.
 */
export default function Login() {
  const { login } = useContext(AuthContext);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false); // Estado para feedback de carregamento no botão

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true); // Ativa o loading
    try {
      await login(email, password);
      // O redirecionamento é feito pelo AuthContext após login bem-sucedido
    } catch (err: any) {
      // Define a mensagem de erro vinda da API ou uma genérica
      setError(err?.response?.data?.message || err?.message || 'Erro ao autenticar. Verifique suas credenciais.');
      setLoading(false); // Desativa o loading em caso de erro
    }
    // Não desativar o loading aqui em caso de sucesso, pois a página vai redirecionar
  };

  return (
    // Container principal centralizado com padding
    <main className="container flex flex-col items-center justify-center min-h-screen py-lg">
      <div className="w-full max-w-md p-lg bg-background-card rounded-lg shadow-md"> {/* Card para o formulário */}
        <h1 className="text-2xl font-bold text-center mb-lg">Login</h1>
        <form onSubmit={handleSubmit} className="space-y-md"> {/* Espaçamento entre elementos do form */}
          {/* Exibição de erro */}
          {error && (
            <div className="p-sm bg-red-100 border border-red-300 text-red-700 rounded text-sm">
              {error}
            </div>
          )}

          {/* Campo de E-mail */}
          <div>
            <label htmlFor="email" className="block text-sm font-medium text-text mb-xs">
              E-mail
            </label>
            <Input
              id="email"
              type="email"
              value={email}
              onChange={e => setEmail(e.target.value)}
              required
              placeholder="seu@exemplo.com"
              aria-describedby={error ? "error-message" : undefined}
            />
          </div>

          {/* Campo de Senha */}
          <div>
            <label htmlFor="password" className="block text-sm font-medium text-text mb-xs">
              Senha
            </label>
            <Input
              id="password"
              type="password"
              value={password}
              onChange={e => setPassword(e.target.value)}
              required
              minLength={6}
              placeholder="••••••"
              aria-describedby={error ? "error-message" : undefined}
            />
            {/* TODO: Adicionar link "Esqueci minha senha" aqui se necessário */}
          </div>

          {/* Botão de Submit */}
          <Button type="submit" variant="primary" className="w-full" disabled={loading}>
            {loading ? 'Entrando...' : 'Entrar'} {/* Feedback de loading no botão */}
          </Button>
        </form>

        {/* Link para Registro */}
        <p className="text-center text-sm text-muted mt-lg">
          Não tem uma conta?{' '}
          <Link to="/registro" className="font-medium text-accent hover:text-accent-hover">
            Registre-se
          </Link>
        </p>
      </div>
    </main>
  );
}

