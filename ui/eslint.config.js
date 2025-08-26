// eslint-disable-next-line unicorn/import-style
import { dirname } from "node:path";
import { fileURLToPath } from "node:url";
import { FlatCompat } from "@eslint/eslintrc";
import eslintPluginUnicorn from 'eslint-plugin-unicorn';

const __filename = fileURLToPath( import.meta.url );
const __dirname = dirname( __filename );

const compat = new FlatCompat( {
	baseDirectory: __dirname,
} );

const eslintConfig = [ ...compat.extends( "next/core-web-vitals", "next/typescript" ),
	{
		ignores: [ ".next/**" ]
	},
	eslintPluginUnicorn.configs.recommended,
	{
		rules: {
			'unicorn/better-regex': 'warn',
			"array-bracket-spacing": [ "error", "always" ],
			"object-curly-spacing": [ "error", "always" ],
			"space-in-parens": [ "error", "always" ],
			"array-bracket-newline": [ "error", { multiline: false, minItems: Infinity } ],
			"object-curly-newline": [ "error", { multiline: false, consistent: true } ],
			"function-paren-newline": [ "error", "consistent" ],
			"indent": [ "error", "tab", { SwitchCase: 1 } ],
			"semi": [ "error", "always" ],
			"no-multiple-empty-lines": [ "error", { max: 2, maxEOF: 1, maxBOF: 1 } ],
			"comma-spacing": [ "error", { "before": false, "after": true } ]
		},
	}, ];

export default eslintConfig;
