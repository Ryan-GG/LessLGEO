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
	eslintPluginUnicorn.configs.recommended,
	{
		rules: {
			'unicorn/better-regex': 'warn',

			// Spacing and formatting
			"array-bracket-spacing": [ "error", "always" ], // [ 1, 2, 3 ]
			"object-curly-spacing": [ "error", "always" ], // { key: value }
			"space-in-parens": [ "error", "always" ], // ( value )
			"array-bracket-newline": [ "error", { multiline: false, minItems: Infinity } ],
			"object-curly-newline": [ "error", { multiline: false, consistent: true } ],
			"function-paren-newline": [ "error", "consistent" ],

			// Indentation: use tabs
			"indent": [ "error", "tab", { SwitchCase: 1 } ],

			// Require semicolons
			"semi": [ "error", "always" ],

			// Limit blank lines
			"no-multiple-empty-lines": [ "error", { max: 2, maxEOF: 1, maxBOF: 1 } ],
		},
	}, ];

export default eslintConfig;
